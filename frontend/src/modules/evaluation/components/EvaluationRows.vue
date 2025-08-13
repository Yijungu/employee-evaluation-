<script setup>
import { toRefs } from 'vue'

const props = defineProps({
  rows: { type: Array, required: true },
  items: { type: Array, default: () => [] },
})
const emit = defineEmits(['add', 'remove'])
const { rows, items } = toRefs(props)
</script>

<template>
  <div>
    <div v-for="(r, idx) in rows" :key="r.id" class="d-flex gap-2 mb-2">
      <select v-model="r.itemId" class="form-select">
        <option :value="null">항목 선택</option>
        <option v-for="it in items" :key="it.id" :value="it.id">{{ it.name }}</option>
      </select>
      <input v-model.number="r.score" type="number" class="form-control" placeholder="점수" style="max-width:120px"/>
      <button class="btn btn-outline-danger" @click="$emit('remove', idx)">삭제</button>
    </div>
    <button class="btn btn-outline-primary btn-sm" @click="$emit('add')">+ 항목 추가</button>
  </div>
</template>


