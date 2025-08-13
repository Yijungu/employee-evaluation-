<script setup>
import { ref, onMounted } from 'vue'
import EvaluationRows from '../components/EvaluationRows.vue'
import { toast } from '../../../services/toast'
import { useEvaluationStore } from '../store/useEvaluationStore'

const store = useEvaluationStore()
const employees = store.employees
const items = store.items
const rowsByEmp = store.rowsByEmp
const raisePctByEmp = store.raisePctByEmp
const memoByEmp = store.memoByEmp
const addRow = (empId) => store.addRow(empId)
const removeRow = (empId, idx) => store.removeRow(empId, idx)
const totalScore = (empId) => store.totalScore(empId)
const raisedSalary = (emp) => store.raisedSalary(emp)

const openMemoEmpId = ref(null)
const openMemo = (empId) => { ensureEmpState(empId); openMemoEmpId.value = empId; new bootstrap.Modal(document.getElementById('memoModal')).show() }
const saveMemoModal = async () => { const empId = openMemoEmpId.value; await store.saveMemoModal(empId); bootstrap.Modal.getInstance(document.getElementById('memoModal')).hide() }

const saveRow = async (emp) => {
  try { await store.saveRow(emp); toast('저장 완료', 'success') } catch (e) { toast(e.message || '저장 실패', 'danger') }
}

const load = async () => { await store.load() }

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
            <EvaluationRows :rows="rowsByEmp[e.id]" :items="items" @add="addRow(e.id)" @remove="(idx) => removeRow(e.id, idx)" />
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



