<script setup>
import { ref, onMounted, computed } from 'vue'
import { getReport } from '../services/report.service'
import Charts from '../components/Charts.vue'

const data = ref(null)

const top10 = computed(() => {
  if (!data.value) return []
  const sorted = [...data.value.employees].sort((a, b) => (b.score || 0) - (a.score || 0))
  const take = Math.max(1, Math.floor(sorted.length * 0.1))
  return sorted.slice(0, take)
})

const bottom10 = computed(() => {
  if (!data.value) return []
  const sorted = [...data.value.employees].sort((a, b) => (a.score || 0) - (b.score || 0))
  const take = Math.max(1, Math.floor(sorted.length * 0.1))
  return sorted.slice(0, take)
})

const currency = n => `₩${Number(n || 0).toLocaleString()}`

const load = async () => {
  const res = await getReport()
  data.value = res.data.data
}

onMounted(load)
onBeforeUnmount(() => {
  if (barChart) barChart.destroy()
  if (lineChart) lineChart.destroy()
})
</script>

<template>
  <div class="container py-4" v-if="data">
    <h3 class="mb-3">사원 평가 보고서</h3>

    <div class="row g-3 mb-3">
      <div class="col-md-3">
        <div class="card text-center"><div class="card-body">
          <div class="text-muted">근무 인원수</div>
          <div class="fs-3 fw-bold">{{ data.workingCount }} 명</div>
        </div></div>
      </div>
      <div class="col-md-3">
        <div class="card text-center"><div class="card-body">
          <div class="text-muted">퇴직 인원수</div>
          <div class="fs-3 fw-bold">{{ data.retiredCount }} 명</div>
        </div></div>
      </div>
      <div class="col-md-3">
        <div class="card text-center"><div class="card-body">
          <div class="text-muted">평균 연봉 상승률</div>
          <div class="fs-3 fw-bold">{{ data.avgRaisePercent }}%</div>
        </div></div>
      </div>
      <div class="col-md-3">
        <div class="card text-center"><div class="card-body">
          <div class="text-muted">총 인건비 변화</div>
          <div class="fs-6">{{ currency(data.totalBasePayroll) }} →</div>
          <div class="fs-5 fw-bold">{{ currency(data.totalRaisedPayroll) }}</div>
        </div></div>
      </div>
    </div>

    <Charts :names="data.employees.map(e=>e.name)"
            :base="data.employees.map(e=>e.baseSalary)"
            :raised="data.employees.map(e=>e.raisedSalary || e.baseSalary)"
            :scores="data.employees.map(e=>e.score || 0)" />

    <div class="row g-3 mt-3">
      <div class="col-md-6">
        <div class="card"><div class="card-header">상위 10% 사원 리스트</div>
          <table class="table mb-0"><thead><tr><th>#</th><th>사원명</th><th>점수</th><th>상승 연봉</th></tr></thead>
            <tbody>
            <tr v-for="(e,i) in top10" :key="e.id"><td>{{ i+1 }}</td><td>{{ e.name }}</td><td>{{ e.score }}</td><td>{{ currency(e.raisedSalary || e.baseSalary) }}</td></tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="col-md-6">
        <div class="card"><div class="card-header">하위 10% 사원 리스트</div>
          <table class="table mb-0"><thead><tr><th>#</th><th>사원명</th><th>점수</th><th>상승 연봉</th></tr></thead>
            <tbody>
            <tr v-for="(e,i) in bottom10" :key="e.id"><td>{{ i+1 }}</td><td>{{ e.name }}</td><td>{{ e.score }}</td><td>{{ currency(e.raisedSalary || e.baseSalary) }}</td></tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>



