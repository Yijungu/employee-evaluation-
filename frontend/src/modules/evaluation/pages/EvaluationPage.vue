<script setup>
import { ref, onMounted } from 'vue'
import { searchEmployees } from '../../employees/services/employees.service'
import { getEvalItems, saveEvaluations, saveMemo, saveRaise, getEvaluations, getRaise } from '../services/evaluation.service'

const employees = ref([])
const items = ref([])

// per-employee state
const rowsByEmp = ref({})          // { empId: [{ id: uniqueKey, itemId: null, score: 0 }] }
const raisePctByEmp = ref({})      // { empId: number }
const memoByEmp = ref({})          // { empId: { projectName, period, detail } }

const ensureEmpState = (empId) => {
  if (!rowsByEmp.value[empId]) rowsByEmp.value[empId] = []
  if (raisePctByEmp.value[empId] == null) raisePctByEmp.value[empId] = 0
  if (!memoByEmp.value[empId]) memoByEmp.value[empId] = { projectName: '', period: '', detail: '' }
}

const addRow = (empId) => {
  ensureEmpState(empId)
  rowsByEmp.value[empId].push({ id: Date.now()+Math.random(), itemId: null, score: 0 })
}
const removeRow = (empId, idx) => {
  rowsByEmp.value[empId].splice(idx, 1)
}

const totalScore = (empId) => rowsByEmp.value[empId]?.reduce((sum, r) => sum + (Number(r.score)||0), 0) || 0
const raisedSalary = (emp) => Math.round((emp.baseSalary||0) * (1 + (Number(raisePctByEmp.value[emp.id])||0)/100))

const openMemoEmpId = ref(null)
const openMemo = (empId) => { ensureEmpState(empId); openMemoEmpId.value = empId; new bootstrap.Modal(document.getElementById('memoModal')).show() }
const saveMemoModal = async () => {
  const empId = openMemoEmpId.value
  if (!empId) return
  const memo = memoByEmp.value[empId]
  if (memo.projectName || memo.period || memo.detail) await saveMemo(empId, memo)
  bootstrap.Modal.getInstance(document.getElementById('memoModal')).hide()
}

const saveRow = async (emp) => {
  ensureEmpState(emp.id)
  // 유효성: 항목 미선택/중복 금지
  const chosen = rowsByEmp.value[emp.id].filter(r => r.itemId != null)
  if (!chosen.length) return alert('평가 항목을 선택해 주세요.')
  const seen = new Set()
  for (const r of chosen) {
    const id = Number(r.itemId)
    if (seen.has(id)) return alert('같은 평가 항목을 중복 선택할 수 없습니다.')
    seen.add(id)
  }
  const list = chosen
    .filter(r => r.score !== null && r.score !== undefined)
    .map(r => ({ item: { id: Number(r.itemId) }, score: Number(r.score) }))
  if (list.length) await saveEvaluations(emp.id, list)
  const pct = Number(raisePctByEmp.value[emp.id])||0
  if (pct) await saveRaise(emp.id, { raisePercent: pct, raisedSalary: raisedSalary(emp) })
  // 저장 후 서버에서 다시 불러와서 화면에 고정
  try {
    const res = await getEvaluations(emp.id)
    const saved = res.data.data || []
    rowsByEmp.value[emp.id] = saved.map(s => ({ id: Date.now()+Math.random(), itemId: s.itemId, score: s.score }))
  } catch {}
  alert('저장 완료')
}

const load = async () => {
  const [empRes, itemRes] = await Promise.all([
    searchEmployees({}),
    getEvalItems(),
  ])
  employees.value = empRes.data.data.content ?? []
  items.value = itemRes.data.data ?? []
  // 직원별 기존 저장 점수 불러오기 + 기본 행 1개 보장
  for (const e of employees.value) {
    addRow(e.id)
    try {
      const res = await getEvaluations(e.id)
      const saved = res.data.data || []
      if (saved.length) {
        rowsByEmp.value[e.id] = saved.map(s => ({ id: Date.now()+Math.random(), itemId: s.itemId, score: s.score }))
      }
      const r = await getRaise(e.id)
      if (r.data.data) {
        raisePctByEmp.value[e.id] = r.data.data.raisePercent || 0
      }
    } catch {}
  }
}

onMounted(load)
</script>

<template>
  <div class="container py-4">
    <h3 class="mb-3">사원 평가</h3>
    <div class="table-responsive">
      <table class="table align-middle">
        <thead>
        <tr>
          <th>사원명</th><th>사번</th><th>연차</th><th>연봉(현재)</th>
          <th style="width:40%">평가 항목 / 점수</th>
          <th>Tot al 점수</th>
          <th style="width:200px">연봉 상승률(%)</th>
          <th>상승 후 연봉</th>
          <th>메모</th>
          <th>저장</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="e in employees" :key="e.id">
          <td>{{ e.name }}</td>
          <td>{{ e.employeeNumber }}</td>
          <td>{{ e.joinYear }}</td>
          <td>₩{{ Number(e.baseSalary).toLocaleString() }}</td>
          <td>
            <div v-for="(r, idx) in rowsByEmp[e.id]" :key="r.id" class="d-flex gap-2 mb-2">
              <select v-model="r.itemId" class="form-select">
                <option :value="null">항목 선택</option>
                <option v-for="it in items" :key="it.id" :value="it.id">{{ it.name }}</option>
              </select>
              <input v-model.number="r.score" type="number" class="form-control" placeholder="점수" style="max-width:120px"/>
              <button class="btn btn-outline-danger" @click="removeRow(e.id, idx)">삭제</button>
            </div>
            <button class="btn btn-outline-primary btn-sm" @click="addRow(e.id)">+ 항목 추가</button>
          </td>
          <td class="fw-bold">{{ totalScore(e.id) }}</td>
          <td>
            <input v-model.number="raisePctByEmp[e.id]" type="number" class="form-control" placeholder="예: 7"/>
          </td>
          <td>₩{{ Number(raisedSalary(e)).toLocaleString() }}</td>
          <td><button class="btn btn-outline-secondary btn-sm" @click="openMemo(e.id)">메모 추가</button></td>
          <td><button class="btn btn-primary btn-sm" @click="saveRow(e)">저장</button></td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- 메모 모달 -->
    <div class="modal fade" id="memoModal" tabindex="-1">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">프로젝트/메모 추가</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body" v-if="openMemoEmpId">
            <div class="row g-2">
              <div class="col-md-6">
                <label class="form-label">프로젝트명</label>
                <input v-model="memoByEmp[openMemoEmpId].projectName" class="form-control" placeholder="예: A장비 설치 프로젝트"/>
              </div>
              <div class="col-md-6">
                <label class="form-label">참여 기간</label>
                <input v-model="memoByEmp[openMemoEmpId].period" class="form-control" placeholder="예: 2024.01 ~ 2024.03"/>
              </div>
              <div class="col-12">
                <label class="form-label">상세 메모</label>
                <textarea v-model="memoByEmp[openMemoEmpId].detail" rows="4" class="form-control" placeholder="프로젝트 기여도, 문제 해결 상황 등"></textarea>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
            <button class="btn btn-primary" @click="saveMemoModal">추가</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>



