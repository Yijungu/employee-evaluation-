<script setup>
import { ref, onMounted, computed } from 'vue'
import api from '../services/api'

// 검색 영역
const keyword = ref('')
const categoryFilter = ref('')
const categories = ref([])
const items = ref([]) // API 목록 (DTO)

const filtered = computed(() => items.value.filter(it => {
  const byKw = !keyword.value || it.name.includes(keyword.value)
  const byCat = !categoryFilter.value || String(it.categoryId) === String(categoryFilter.value)
  return byKw && byCat
}))

// 등록/수정 폼 상태
const showForm = ref(false)
const form = ref({ id: null, name: '', categoryId: '', maxScore: 10, enabled: true, description: '' })
const startCreate = () => { showForm.value = true; form.value = { id: null, name: '', categoryId: '', maxScore: 10, enabled: true, description: '' } }
const startEdit = (row) => { showForm.value = true; form.value = { id: row.id, name: row.name, categoryId: row.categoryId || '', maxScore: row.maxScore, enabled: !!row.enabled, description: row.description || '' } }

const newCategoryName = ref('')

const load = async () => {
  const [catRes, itemRes] = await Promise.all([
    api.get('/categories'),
    api.get('/items')
  ])
  categories.value = catRes.data.data || []
  const list = itemRes.data.data || []
  // 중복 방지 (id 기준)
  items.value = Array.from(new Map(list.map(i => [i.id, i])).values())
}

const saveItem = async () => {
  const payload = {
    name: form.value.name,
    maxScore: Number(form.value.maxScore),
    enabled: !!form.value.enabled,
    description: form.value.description,
    categoryId: form.value.categoryId ? Number(form.value.categoryId) : null,
  }
  if (form.value.id) await api.put(`/items/${form.value.id}`, payload)
  else await api.post('/items', payload)
  await load(); showForm.value = false; alert('저장 완료')
}

const addCategory = async () => {
  if (!newCategoryName.value) return
  await api.post('/categories', { name: newCategoryName.value })
  newCategoryName.value = ''
  await load()
}

onMounted(load)
</script>

<template>
  <div class="container py-4">
    <h3 class="mb-3">평가항목 조회</h3>
    <div class="row g-2 align-items-end mb-2">
      <div class="col-md-5">
        <input v-model="keyword" class="form-control" placeholder="평가 항목명 검색" />
      </div>
      <div class="col-md-4">
        <select v-model="categoryFilter" class="form-select">
          <option value="">카테고리</option>
          <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
        </select>
      </div>
      <div class="col-md-3 d-grid">
        <button class="btn btn-primary" @click="load">검색</button>
      </div>
    </div>

    <div class="table-responsive">
      <table class="table table-striped align-middle">
        <thead><tr><th>#</th><th>평가 항목명</th><th>카테고리</th><th>점수 배점</th><th>사용 여부</th><th>액션</th></tr></thead>
        <tbody>
          <tr v-for="(it, i) in filtered" :key="it.id">
            <td>{{ i+1 }}</td>
            <td>{{ it.name }}</td>
            <td>{{ it.categoryName }}</td>
            <td>{{ it.maxScore }}점</td>
            <td>{{ it.enabled ? '사용' : '미사용' }}</td>
            <td><button class="btn btn-sm btn-outline-primary" @click="startEdit(it)">수정</button></td>
          </tr>
        </tbody>
      </table>
    </div>

    <button class="btn btn-success mt-2" @click="startCreate">신규 항목 등록</button>

    <div class="mt-4" v-if="showForm">
      <h5 class="mb-3">평가항목 등록</h5>
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
          <button class="btn btn-secondary" @click="showForm=false">목록</button>
          <button class="btn btn-primary" @click="saveItem">저장</button>
        </div>
      </div>
    </div>

    <!-- 새 카테고리 모달 -->
    <div class="modal fade" id="addCat" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">새 카테고리 추가</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <label class="form-label">카테고리명</label>
            <input v-model="newCategoryName" class="form-control" placeholder="예: 품질관리" />
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
            <button class="btn btn-primary" data-bs-dismiss="modal" @click="addCategory">추가</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


