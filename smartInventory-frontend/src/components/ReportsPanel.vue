<template>
  <section class="card">
    <h2>Отчёты</h2>
    <div class="controls">
      <label>Начало: <input type="date" v-model="from" /></label>
      <label>Конец: <input type="date" v-model="to" /></label>
      <button @click="movement">Движения</button>
    </div>
    <div v-if="movementData.length">
      <ul>
        <li v-for="m in movementData" :key="m.id">{{ m.id }} - product {{ m.productId }} - {{ m.type }} {{ m.quantity }} - {{ m.createdAt }}</li>
      </ul>
    </div>
    <h3>Сводка</h3>
    <div class="controls">
      <button @click="summary">Обновить сводку</button>
      <label>Минимальный остаток: <input type="number" v-model.number="threshold" min="0" /></label>
      <button @click="low">Применить</button>
    </div>
    <div v-if="summaryData.length">
      <ul>
        <li v-for="s in summaryData" :key="s.productId">Товар {{ s.productId }} — баланс: {{ s.balance }}</li>
      </ul>
    </div>
    <div v-if="lowData.length">
      <h4>Low</h4>
      <ul>
        <li v-for="l in lowData" :key="l.productId">Товар {{ l.productId }} — баланс: {{ l.balance }}</li>
      </ul>
    </div>
    <div class="msg">{{ msg }}</div>
  </section>
</template>

<script setup>
import { ref } from 'vue'
const REPORT_URL = 'http://localhost:8080/reports'

const from = ref('')
const to = ref('')
const movementData = ref([])
const summaryData = ref([])
const lowData = ref([])
const threshold = ref(5)
const msg = ref('')

async function movement() {
  msg.value = 'Загрузка...'
  const params = new URLSearchParams()
  if (from.value) params.append('from', from.value)
  if (to.value) params.append('to', to.value)
  try {
    const res = await fetch(REPORT_URL + '/movement?' + params.toString())
    if (!res.ok) throw new Error('HTTP ' + res.status)
    movementData.value = await res.json()
    msg.value = ''
  } catch (e) { msg.value = 'Ошибка: ' + e.message }
}

async function summary() {
  msg.value = 'Загрузка...'
  try {
    const res = await fetch(REPORT_URL + '/stock-summary')
    if (!res.ok) throw new Error('HTTP ' + res.status)
    summaryData.value = await res.json()
    msg.value = ''
  } catch (e) { msg.value = 'Ошибка: ' + e.message }
}

async function low() {
  msg.value = 'Загрузка...'
  try {
    const res = await fetch(REPORT_URL + '/stock-low?threshold=' + threshold.value)
    if (!res.ok) throw new Error('HTTP ' + res.status)
    lowData.value = await res.json()
    msg.value = ''
  } catch (e) { msg.value = 'Ошибка: ' + e.message }
}
</script>

<style scoped>
.card{background:#fff;padding:12px;border-radius:8px;box-shadow:0 1px 3px rgba(0,0,0,.06)}
.controls{margin-bottom:8px;display:flex;gap:8px;flex-wrap:wrap}
.msg{color:#666;margin-top:8px}
</style>
