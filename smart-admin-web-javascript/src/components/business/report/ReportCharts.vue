<!--
  * 通用报告图表组件
  *
  * @Author: 1024创新实验室
  * @Date: 2024-12-07
  * @Copyright: 1024创新实验室 (https://1024lab.net)
-->
<template>
  <div class="report-charts">
    <a-row :gutter="[16, 16]">
      <!-- 健康计划类型分布饼图 -->
      <a-col :span="12" v-if="chartData.planTypePie">
        <a-card size="small" :title="chartData.planTypePie.title">
          <div 
            :id="'planTypePie-' + componentId" 
            style="height: 300px;"
          ></div>
        </a-card>
      </a-col>

      <!-- 执行率对比柱状图 -->
      <a-col :span="12" v-if="chartData.executionRateBar">
        <a-card size="small" :title="chartData.executionRateBar.title">
          <div 
            :id="'executionRateBar-' + componentId" 
            style="height: 300px;"
          ></div>
        </a-card>
      </a-col>

      <!-- 健康状态分布饼图 -->
      <a-col :span="12" v-if="chartData.healthStatusPie">
        <a-card size="small" :title="chartData.healthStatusPie.title">
          <div 
            :id="'healthStatusPie-' + componentId" 
            style="height: 300px;"
          ></div>
        </a-card>
      </a-col>

      <!-- 预留位置给其他图表 -->
      <a-col :span="12">
        <a-card size="small" title="更多统计">
          <div style="height: 300px; display: flex; align-items: center; justify-content: center; color: #999;">
            📊 更多图表敬请期待
          </div>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup>
import { computed, onMounted, watch, nextTick } from 'vue';
import * as echarts from 'echarts';

// 定义组件属性
const props = defineProps({
  chartData: {
    type: Object,
    default: () => ({})
  }
});

// 生成唯一组件ID
const componentId = Math.random().toString(36).substr(2, 9);

// 存储图表实例
let planTypePieChart = null;
let executionRateBarChart = null;
let healthStatusPieChart = null;

// 健康计划类型分布饼图配置
const pieChartOption = computed(() => {
  const data = props.chartData.planTypePie?.data || [];
  
  return {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'horizontal',
      bottom: 10,
      data: data.map(item => item.name)
    },
    series: [
      {
        name: '计划类型',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '45%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 5,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 14,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: data
      }
    ],
    color: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de']
  };
});

// 执行率对比柱状图配置
const barChartOption = computed(() => {
  const chartInfo = props.chartData.executionRateBar || {};
  const categories = chartInfo.categories || [];
  const series = chartInfo.series || [];
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      },
      formatter: function(params) {
        let result = params[0].name + '<br/>';
        params.forEach(param => {
          result += param.marker + ' ' + param.seriesName + ': ' + param.value.toFixed(1) + '%<br/>';
        });
        return result;
      }
    },
    legend: {
      data: series.map(s => s.name),
      bottom: 10
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      containLabel: true
    },
    xAxis: [
      {
        type: 'category',
        data: categories,
        axisTick: {
          alignWithLabel: true
        }
      }
    ],
    yAxis: [
      {
        type: 'value',
        max: 100,
        axisLabel: {
          formatter: '{value}%'
        }
      }
    ],
    series: series.map(s => ({
      ...s,
      type: 'bar',
      barWidth: '60%'
    })),
    color: ['#5470c6', '#91cc75']
  };
});

// 健康状态分布饼图配置
const statusPieOption = computed(() => {
  const data = props.chartData.healthStatusPie?.data || [];
  
  return {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c}匹 ({d}%)'
    },
    legend: {
      orient: 'horizontal',
      bottom: 10,
      data: data.map(item => item.name)
    },
    series: [
      {
        name: '健康状态',
        type: 'pie',
        radius: '60%',
        center: ['50%', '45%'],
        itemStyle: {
          borderRadius: 5,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          formatter: '{b}: {c}匹'
        },
        data: data
      }
    ],
    color: ['#52c41a', '#faad14', '#ff7875', '#40a9ff']
  };
});

// 初始化图表
const initCharts = async () => {
  await nextTick();
  
  // 初始化健康计划类型分布饼图
  if (props.chartData.planTypePie) {
    const pieElement = document.getElementById('planTypePie-' + componentId);
    if (pieElement && !planTypePieChart) {
      planTypePieChart = echarts.init(pieElement);
      planTypePieChart.setOption(pieChartOption.value);
    }
  }
  
  // 初始化执行率对比柱状图
  if (props.chartData.executionRateBar) {
    const barElement = document.getElementById('executionRateBar-' + componentId);
    if (barElement && !executionRateBarChart) {
      executionRateBarChart = echarts.init(barElement);
      executionRateBarChart.setOption(barChartOption.value);
    }
  }
  
  // 初始化健康状态分布饼图
  if (props.chartData.healthStatusPie) {
    const statusElement = document.getElementById('healthStatusPie-' + componentId);
    if (statusElement && !healthStatusPieChart) {
      healthStatusPieChart = echarts.init(statusElement);
      healthStatusPieChart.setOption(statusPieOption.value);
    }
  }
};

// 更新图表数据
const updateCharts = () => {
  if (planTypePieChart && props.chartData.planTypePie) {
    planTypePieChart.setOption(pieChartOption.value, true);
  }
  if (executionRateBarChart && props.chartData.executionRateBar) {
    executionRateBarChart.setOption(barChartOption.value, true);
  }
  if (healthStatusPieChart && props.chartData.healthStatusPie) {
    healthStatusPieChart.setOption(statusPieOption.value, true);
  }
};

// 监听图表数据变化
watch(() => props.chartData, () => {
  if (planTypePieChart || executionRateBarChart || healthStatusPieChart) {
    updateCharts();
  } else {
    initCharts();
  }
}, { deep: true });

onMounted(() => {
  initCharts();
});
</script>

<style scoped>
.report-charts {
  margin-top: 20px;
}

:deep(.ant-card-head-title) {
  font-size: 14px;
  font-weight: 500;
}

:deep(.ant-card-body) {
  padding: 16px;
}
</style>