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
            formatter: '{value} E/KWh'
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
    const [max, setMax] = useState<number>(0);
    const [min, setMin] = useState<number>(0);
    const [avg, setAvg] = useState<number>(0);
    const [option, setOption] = useState(DEFAULT_OPTION);


    useEffect(() => {
        const fetchData = async () => {
            const result = await fetch('/prices', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            result.json().then((data) => {
                let sum = 0;
                let max = 0;
                let min = Number.POSITIVE_INFINITY;
                const prices: HistoricPrice[] = [];
                for (let i = 0; i < data.length; i++) {
                    // Find Max Price
                    if (data[i].price > max) {
                        max = data[i].price;
                    }
                    // Find Min Price
                    if (data[i].price < min) {
                        min = data[i].price;
                    }
                    // Find Avg Price
                    sum += data[i].price;

                    const price = new HistoricPrice(data[i].year, data[i].month, data[i].day, data[i].hour, data[i].price /100, data[i].coin);
                    prices.push(price);
                }
                setData(prices);
                setAvg((sum / data.length)/100);
                setMax(max/100);
                setMin(min/100);
            });

            // Calculate Avg Price
        };
        fetchData();
    }, []);



    useEffect(() => {
        const xAxisData: string[] = [];
        const yAxisData: number[] = [];
        for (let i = 0; i < data.length; i++) {
            xAxisData.push(data[i].hour + ":00");
            yAxisData.push();
        }
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
                    formatter: '{value} E/KWh'
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
                    lte: avg,
                    color: 'green'
                  },
                  {
                    gt: avg,
                    lte: max,
                    color: 'red'
                  }
                ]
              },
            series: [
                {
                    name: 'Electricity',
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
                          color: 'rgba(0, 173, 177, 0.4)'
                        },
                        data: [
                          ...data.filter((item) => item.price <= avg).map((item) => {
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
                    name: 'Cheap',
                    type: 'line',
                    data: [],
                
                    markArea: {
                        itemStyle: {
                          color: 'rgba(255, 173, 177, 0.4)'
                        },
                        data: [
                          ...data.filter((item) => item.price >= avg).map((item) => {
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
    }, [data, max, min, avg]);





    return <ReactECharts option={option}     style={{ height: 400 }}    />;
};

export default Page;