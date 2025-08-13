<script setup>
import { toRefs, computed } from 'vue'

const props = defineProps({
  form: { type: Object, required: true },
  categories: { type: Array, default: () => [] },
})
const emit = defineEmits(['save', 'add-category'])
const { form, categories } = toRefs(props)
const isEdit = computed(() => !!form.value.id)
</script>

<template>
  <div class="row g-2 align-items-end">
    <div class="col-md-5">
      <label class="form-label">평가 항목명</label>
      <input v-model="form.name" class="form-control" placeholder="예: 신제품 아이디어 기여도" />
    </div>
    <div class="col-md-4">
      <label class="form-label">카테고리</label>
      <div class="input-group">
        <select v-model="form.categoryId" class="form-select">
          <option value="">선택</option>
          <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
        </select>
        <button class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#addCat">+ 새 카테고리</button>
      </div>
    </div>
    <div class="col-md-3">
      <label class="form-label">배점(점수)</label>
      <input v-model.number="form.maxScore" type="number" class="form-control" placeholder="예: 10, 20" />
    </div>
    <div class="col-md-3">
      <label class="form-label">사용 여부</label>
      <select v-model="form.enabled" class="form-select">
        <option :value="true">사용</option>
        <option :value="false">미사용</option>
      </select>
    </div>
    <div class="col-12">
      <label class="form-label">비고</label>
      <input v-model="form.description" class="form-control" placeholder="평가 기준 또는 설명 입력" />
    </div>
    <div class="col-12 d-flex gap-2 mt-2 justify-content-end">
      <button class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
      <button class="btn btn-primary" @click="$emit('save')">{{ isEdit ? '수정' : '저장' }}</button>
    </div>
  </div>
</template>


