<script setup>
import { ref, onBeforeUnmount, watch } from 'vue'
import Chart from 'chart.js/auto'

const props = defineProps({
  names: { type: Array, default: () => [] },
  base: { type: Array, default: () => [] },
  raised: { type: Array, default: () => [] },
  scores: { type: Array, default: () => [] },
})

const currency = n => `₩${Number(n || 0).toLocaleString()}`

const barRef = ref(null)
const lineRef = ref(null)
let barChart, lineChart

const draw = () => {
  if (barChart) barChart.destroy()
  if (lineChart) lineChart.destroy()
  barChart = new Chart(barRef.value.getContext('2d'), {
    type: 'bar',
    data: { labels: props.names, datasets: [
      { label: '이전 연봉', data: props.base, backgroundColor: '#74b9ff' },
      { label: '상승 후 연봉', data: props.raised, backgroundColor: '#a29bfe' },
    ]},
    options: {
      responsive: true, maintainAspectRatio: false,
      plugins: { legend: { position: 'top' }, tooltip: { callbacks: { label: c => `${c.dataset.label}: ${currency(c.parsed.y)}` } } },
      scales: { y: { beginAtZero: true, ticks: { callback: v => currency(v) } } },
    },
  })
  lineChart = new Chart(lineRef.value.getContext('2d'), {
    type: 'line',
    data: { labels: props.names, datasets: [
      { label: '직원별 점수 추이', data: props.scores, fill: true, tension: 0.2, cubicInterpolationMode: 'monotone', borderColor: '#ff7675', backgroundColor: 'rgba(255,118,117,0.2)', pointBackgroundColor: '#ff7675' },
    ]},
    options: { responsive: true, maintainAspectRatio: false, scales: { y: { suggestedMin: 0, suggestedMax: 100 } } },
  })
}

watch(() => [props.names, props.base, props.raised, props.scores], draw, { deep: true })
onBeforeUnmount(() => { if (barChart) barChart.destroy(); if (lineChart) lineChart.destroy() })
</script>

<template>
  <div class="row g-3">
    <div class="col-md-6">
      <div style="height: 320px" class="card p-3"><canvas ref="barRef"></canvas></div>
    </div>
    <div class="col-md-6">
      <div style="height: 320px" class="card p-3"><canvas ref="lineRef"></canvas></div>
    </div>
  </div>
</template>


