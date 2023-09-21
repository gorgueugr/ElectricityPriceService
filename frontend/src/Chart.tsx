import React, { useEffect, useState } from 'react';
import ReactECharts from 'echarts-for-react';

class HistoricPrice {
    constructor(public year: number, public month: number, public day: number, public hour: number, public price: number, public coin: string) { }
}

const DEFAULT_OPTION = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'cross'
        }
    },
    xAxis: {
        type: 'category',
        boundaryGap: true,
        data: ['00:00', '01:00', '02:00', '03:00', '04:00', '05:00', "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"]
    },
    yAxis: {
        type: 'value',
        axisLabel: {
            formatter: '{value} €/KWh'
        },
        axisPointer: {
            snap: true
        }
    },
    series: [
        {
            name: 'Electricity',
            type: 'line',
            smooth: true,
            data: [],
        },

    ]
};



const Page: React.FC = () => {
    const [data, setData] = useState<HistoricPrice[]>([]);
    const [option, setOption] = useState(DEFAULT_OPTION);
    const [max, setMax] = useState(0);
    const [min, setMin] = useState(0);
    const [avg, setAvg] = useState(0);

    useEffect(() => {
        const fetchData = async () => {
            const result = await fetch('/prices', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            result.json().then((data) => {
                const prices: HistoricPrice[] = [];
                for (let i = 0; i < data.length; i++) {
                    const price = new HistoricPrice(data[i].year, data[i].month, data[i].day, data[i].hour, data[i].price /100, data[i].coin);
                    prices.push(price);
                }
                setData(prices);
                setMax(Math.max(...prices.map((item) => item.price)));
                setMin(Math.min(...prices.map((item) => item.price)));
                setAvg(prices.reduce((a, b) => a + b.price, 0) / prices.length);
            });

            // Calculate Avg Price
        };
        fetchData();
    }, []);



    useEffect(() => {

      // const min = Math.min(...data.map((item) => item.price));
      // const max = Math.max(...data.map((item) => item.price));
      if (data.length === 0) {
        return;
      }
      const sorted = [...data].sort((a, b) => a?.price - b?.price);
      // const sum = data.reduce((a, b) => a + b.price, 0);
      const q1Pos = Math.floor((sorted.length - 1) * 0.25) ;
      const q3Pos = Math.floor((sorted.length - 1) * 0.75) ;
      const q1 = sorted[q1Pos].price;
      const q3 = sorted[q3Pos].price;

        const newOption = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross'
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: true,
                // data: ['00:00', '01:00', '02:00', '03:00', '04:00', '05:00', "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"]
                data: data.map((item) => item.hour + ":00"),
            },
            yAxis: {
                type: 'value',
                axisLabel: {
                    formatter: '{value} €/KWh'
                },
                axisPointer: {
                    snap: true
                }
            },
            visualMap: {
                show: false,
                dimension: 1,
                pieces: [
                  {
                    lte: q1 + 1,
                    color: 'green'
                  },
                  {
                    lte: q3,
                    gt: q1,
                    color: 'yellow'
                  },
                  {
                    gt: q3,
                    color: 'red'
                  }
                ]
              },
            series: [
                {
                    name: 'Precio €/KWh',
                    type: 'line',
                    smooth: true,
                    data: data.map((item) => item.price),
                },
                {
                    name: 'Cheap',
                    type: 'line',
                    data: [],
                
                    markArea: {
                        itemStyle: {
                          color: 'rgb(177, 220, 181)'
                        },
                        data: [
                          ...data.filter((item) => item.price <= q1).map((item) => {
                            return [
                              {
                                xAxis: item.hour + ":00"
                              },
                              {
                                xAxis: (item.hour+1) + ":00"
                              }
                            ]
                          })
                        ]
                      },
                },
                {
                  name: 'Mid',
                  type: 'line',
                  data: [],
              
                  markArea: {
                      itemStyle: {
                        color: 'rgb(254, 198, 57)'
                      },
                      data: [
                        ...data.filter((item) => item.price < q3 && item.price > q1).map((item) => {
                          return [
                            {
                              xAxis: item.hour + ":00"
                            },
                            {
                              xAxis: (item.hour+1) + ":00"
                            }
                          ]
                        })
                      ]
                    },
              },
                {
                    name: 'Expensive',
                    type: 'line',
                    data: [],
                
                    markArea: {
                        itemStyle: {
                          color: 'rgb(255, 144, 42)'
                        },
                        data: [
                          ...data.filter((item) => item.price >= q3).map((item) => {
                            return [
                              {
                                xAxis: item.hour + ":00"
                              },
                              {
                                xAxis: (item.hour+1) + ":00"
                              }
                            ]
                          })
                        ]
                      },
                }
               
            ]
        };
        // @ts-ignore
        setOption(newOption);
    }, [data]);





    return <>
      <ReactECharts option={option}     style={{ height: 400, width: '100%', }}    />
      <div style={{ width: '100%', display: 'flex', gap: '1em', justifyContent: 'center'}}>
        <div>
          <h4>Precio mínimo</h4>
          <p>{min.toFixed(2)} €/KWh</p>
        </div>
        <div>
          <h4>Precio medio</h4>
          <p>{avg.toFixed(2)} €/KWh</p>
        </div>
        <div>
          <h4>Precio máximo</h4>
          <p>{max.toFixed(2)} €/KWh</p>
        </div>
      </div>
    </>;
};

export default Page;