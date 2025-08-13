<script setup>
import { onMounted } from 'vue'
import EvalItemForm from '../components/EvalItemForm.vue'
import { toast } from '../../../services/toast'
import { useEvalItemsStore } from '../store/useEvalItemsStore'
import { useValidation } from '../../../composables/useValidation'

const store = useEvalItemsStore()
const { validateEvalItem } = useValidation()
const keyword = store.$state.keyword
const categoryFilter = store.$state.categoryFilter
const categories = store.$state.categories
const items = store.$state.items
const filtered = store.filtered
const showForm = store.$state.showForm
const form = store.$state.form
const startCreate = store.startCreate
const startEdit = store.startEdit
const newCategoryName = store.$state.newCategoryName

const load = async () => { await store.load() }

const saveItem = async () => {
  const { valid, message } = validateEvalItem(store.form)
  if (!valid) return toast(message, 'danger')
  await store.saveItem(); toast('저장 완료', 'success')
}

const addCategory = async () => { await store.addCategory(); toast('카테고리 추가 완료', 'success') }

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
      <EvalItemForm :form="form" :categories="categories" @save="saveItem" />
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



