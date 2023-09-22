import React, { useEffect, useState } from 'react';
import ReactECharts from 'echarts-for-react';

class HistoricPrice {
  constructor(public year: number, public month: number, public day: number, public hour: number, public price: number, public coin: string) { }
}

const DEFAULT_OPTION = {
  xAxis: {
    type: 'category',
    boundaryGap: false,
    axisPointer: {
      snap: true
    },
    axisLabel: {
      interval: 1,
      formatter: (value: Date) => value.getHours() + ":00",
    },
  },
  yAxis: {
    type: 'value',
    axisLabel: {
      formatter: (value: number) => value.toFixed(2) + ' €/KWh',
    },
    axisPointer: {
      snap: true
    }
  },
  series: []
};

const Page: React.FC = () => {
  const [data, setData] = useState<HistoricPrice[]>([]);
  const [option, setOption] = useState(DEFAULT_OPTION);
  const [max, setMax] = useState(0);
  const [min, setMin] = useState(0);
  const [avg, setAvg] = useState(0);

  const fetchData = async () => {
    const result = await fetch('/prices', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    const prices: HistoricPrice[] = [];
    result.json().then((data) => {
      for (let i = 0; i < data.length; i++) {
        const price = new HistoricPrice(data[i].year, data[i].month, data[i].day, data[i].hour, data[i].price / 100, data[i].coin);
        prices.push(price);
      }
      setData(prices);
      setMax(Math.max(...prices.map((item) => item.price)));
      setMin(Math.min(...prices.map((item) => item.price)));
      setAvg(prices.reduce((a, b) => a + b.price, 0) / prices.length);
    });
    return prices;
  };


  const updateOption = () => {
    if (data.length === 0) {
      return;
    }
    // Calculate the quartiles
    const sorted = [...data].sort((a, b) => a?.price - b?.price);
    const q1Pos = Math.floor((sorted.length - 1) * 0.25);
    const q3Pos = Math.floor((sorted.length - 1) * 0.75);
    const q1 = sorted[q1Pos].price;
    const q3 = sorted[q3Pos].price;
    //

    // const finalData = data.map((item) => item.price)
    const timeData = data.map((item) => [new Date(item.year, item.month, item.day, item.hour, 0, 0, 0).getTime(), item.price])

    const newOption = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross'
        }
      },
      xAxis: {
        type: 'time',
        boundaryGap: false,
        splitNumber: 24,
        axisLabel: {
          show: true,
          formatter: (value: Date) => {
            // Format the timestamp to display only hours
            const date = new Date(value);
            const hours = date.getHours();
            return String(hours).padStart(2, '0') + ':00';
          }
        },

      },
      yAxis: {
        type: 'value',
        axisLabel: {
          valueFormatter: (value: number) => value.toFixed(2) + ' €/KWh',

        },
        axisPointer: {
          snap: false
        }
      },
      visualMap: {
        show: false,
        dimension: 1,
        pieces: [
          {
            lte: q1,
            color: 'rgb(177, 220, 181)'
          },
          {
            lte: q3,
            gt: q1,
            color: 'rgb(254, 198, 57)'
          },
          {
            gt: q3,
            color: 'rgb(255, 144, 42)'
          }
        ]
      },
      series: [
        {
          name: 'Precio',
          type: 'line',
          smooth: true,
          data: timeData,
          lineStyle: {
            width: 3,

          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross'
            },
            valueFormatter: (value: number) => (value).toFixed(2) + ' €/KWh',
          },
        },

      ]
    };
    // @ts-ignore
    setOption(newOption);
  }

  useEffect(() => {
    fetchData();
  }, []);

  useEffect(() => {
    updateOption()
  }, [data]);




  return <>
    <ReactECharts option={option} style={{ height: 400, width: '100%', }} />
    <div style={{ width: '100%', display: 'flex', gap: '1em', justifyContent: 'center' }}>
      <div style={{ border: '3px solid rgb(177, 220, 181)', borderRadius: '5px', padding: '1em'}}>
        <h4>Precio mínimo</h4>
        <p>{min.toFixed(2)} €/KWh</p>
      </div>
      <div style={{ border: '3px solid rgb(254, 198, 57)', borderRadius: '5px', padding: '1em'}}>
        <h4>Precio medio</h4>
        <p>{avg.toFixed(2)} €/KWh</p>
      </div>
      <div style={{ border: '3px solid rgb(255, 144, 42)', borderRadius: '5px', padding: '1em'}}>
        <h4>Precio máximo</h4>
        <p>{max.toFixed(2)} €/KWh</p>
      </div>
    </div>
  </>;
};

export default Page;