import Chart from "chart.js";
export const chartjs = {
    createChart(chartId, chartData) {
        const ctx = document.getElementById(chartId);
        const myChart = new Chart(ctx, {
            type: chartData.type,
            data: chartData.data,
            options: chartData.options,
        });
        console.log(myChart);
    },
};
