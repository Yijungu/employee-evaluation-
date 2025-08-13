<script setup>
import { toRefs } from 'vue'

const props = defineProps({
  form: { type: Object, required: true },
})
const emit = defineEmits(['save'])
const { form } = toRefs(props)

const onSave = () => emit('save')
</script>

<template>
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
          <button class="btn btn-primary" @click="onSave">저장</button>
        </div>
      </div>
    </div>
  </div>
  
</template>


