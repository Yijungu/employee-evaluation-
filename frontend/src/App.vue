<script setup>
</script>

<template>
  <div>
    <nav class="navbar navbar-expand-lg bg-body-tertiary px-3">
      <a class="navbar-brand" href="#">사원 평가 시스템</a>
      <div class="navbar-nav gap-2">
        <RouterLink class="nav-link" to="/employees">사원 조회</RouterLink>
        <RouterLink class="nav-link" to="/eval-items">평가항목 관리</RouterLink>
        <RouterLink class="nav-link" to="/evaluation">사원 평가</RouterLink>
        <RouterLink class="nav-link" to="/report">평가 보고서</RouterLink>
      </div>
    </nav>
    <main class="container py-3">
      <router-view />
    </main>
  </div>
  
</template>

<style scoped>
</style>
