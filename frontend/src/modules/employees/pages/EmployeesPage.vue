<script setup>
import { ref, onMounted } from 'vue'
import { searchEmployees, saveEmployee, uploadEmployeesExcel } from '../../../services/api'

const filters = ref({ name: '', status: '', region: '' })
const employees = ref([])
const loading = ref(false)
const form = ref({ id: null, employeeNumber: '', name: '', joinYear: new Date().getFullYear(), baseSalary: 0, position: '', workStatus: '근무', workRegion: '국내', email: '', phone: '', notes: '' })

const load = async () => {
  loading.value = true
  try {
    const { data } = await searchEmployees(filters.value)
    employees.value = data.data?.content ?? []
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  form.value = { id: null, employeeNumber: '', name: '', joinYear: new Date().getFullYear(), baseSalary: 0, position: '', workStatus: '근무', workRegion: '국내', email: '', phone: '', notes: '' }
}

const save = async () => {
  await saveEmployee(form.value)
  await load()
  alert('저장 완료')
}

const onExcel = async (e) => {
  const file = e.target.files?.[0]
  if (!file) return
  try {
    const { data } = await uploadEmployeesExcel(file)
    await load()
    alert(`업로드 완료: ${data.data}건 저장`)
  } catch (err) {
    alert('엑셀 업로드 실패. 파일 형식과 내용을 확인해 주세요.')
  } finally {
    e.target.value = ''
  }
}


onMounted(load)
</script>

<template>
  <div class="container py-4">
    <h3 class="mb-3">사원 조회</h3>
    <div class="row g-2 align-items-end">
      <div class="col-md-4">
        <label class="form-label">사원명</label>
        <input v-model="filters.name" class="form-control" placeholder="이름" />
      </div>
      <div class="col-md-3">
        <label class="form-label">근무 상태</label>
        <select v-model="filters.status" class="form-select">
          <option value="">전체</option>
          <option>근무</option>
          <option>휴직</option>
          <option>퇴직</option>
        </select>
      </div>
      <div class="col-md-3">
        <label class="form-label">근무 지역</label>
        <select v-model="filters.region" class="form-select">
          <option value="">전체</option>
          <option>국내</option>
          <option>미국법인</option>
          <option>유럽법인</option>
        </select>
      </div>
      <div class="col-md-2 d-grid">
        <button class="btn btn-primary" @click="load" :disabled="loading">검색</button>
      </div>
    </div>

    <div class="mt-3 d-flex gap-2 align-items-center">
      <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#empModal" @click="resetForm()">신규 사원 등록</button>
      <label class="btn btn-outline-secondary mb-0">
        사원 등록(Excel)
        <input type="file" accept=".xlsx,.xls" class="d-none" @change="onExcel" />
      </label>
    </div>

    <div class="table-responsive mt-3">
      <table class="table table-striped align-middle">
        <thead>
        <tr>
          <th>#</th><th>사원명</th><th>사번</th><th>입사년도</th><th>급여(₩)</th><th>직급</th><th>근무 상태</th><th>근무 지역</th><th>상세</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(e, i) in employees" :key="e.id">
          <td>{{ i + 1 }}</td>
          <td>{{ e.name }}</td>
          <td>{{ e.employeeNumber }}</td>
          <td>{{ e.joinYear }}</td>
          <td>₩{{ Number(e.baseSalary).toLocaleString() }}</td>
          <td>{{ e.position }}</td>
          <td>{{ e.workStatus }}</td>
          <td>{{ e.workRegion }}</td>
          <td>
            <button class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#empModal" @click="form = { ...e }">상세</button>
          </td>
        </tr>
        <tr v-if="!employees.length">
          <td colspan="9" class="text-center text-muted">데이터가 없습니다</td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="empModal" tabindex="-1">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">사원 등록/수정</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div class="row g-2">
              <div class="col-md-4">
                <label class="form-label">사원명</label>
                <input v-model="form.name" class="form-control" />
              </div>
              <div class="col-md-4">
                <label class="form-label">사번</label>
                <input v-model="form.employeeNumber" class="form-control" />
              </div>
              <div class="col-md-4">
                <label class="form-label">급여</label>
                <input v-model.number="form.baseSalary" type="number" class="form-control" />
              </div>
              <div class="col-md-4">
                <label class="form-label">입사년도</label>
                <input v-model.number="form.joinYear" type="number" class="form-control" />
              </div>
              <div class="col-md-4">
                <label class="form-label">직급</label>
                <input v-model="form.position" class="form-control" />
              </div>
              <div class="col-md-4">
                <label class="form-label">근무 상태</label>
                <select v-model="form.workStatus" class="form-select">
                  <option>근무</option>
                  <option>휴직</option>
                  <option>퇴직</option>
                </select>
              </div>
              <div class="col-md-4">
                <label class="form-label">근무 지역</label>
                <select v-model="form.workRegion" class="form-select">
                  <option>국내</option>
                  <option>미국법인</option>
                  <option>유럽법인</option>
                </select>
              </div>
              <div class="col-md-4">
                <label class="form-label">이메일</label>
                <input v-model="form.email" type="email" class="form-control" />
              </div>
              <div class="col-md-4">
                <label class="form-label">전화번호</label>
                <input v-model="form.phone" class="form-control" />
              </div>
              <div class="col-12">
                <label class="form-label">메모</label>
                <textarea v-model="form.notes" rows="2" class="form-control"></textarea>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
            <button class="btn btn-primary" @click="save">저장</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container { max-width: 1100px; }
</style>



