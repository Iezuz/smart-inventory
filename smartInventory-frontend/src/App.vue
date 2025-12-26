<template>
  <div class="container">
    <header>
      <h1>Smart Inventory</h1>
      <p>Перемещения и отчёты</p>
    </header>

    <main>
      <InventoryPanel />
      <TransactionsPanel />
      <ReportsPanel />
    </main>

  </div>
</template>

<script setup>
import InventoryPanel from './components/InventoryPanel.vue'
import TransactionsPanel from './components/TransactionsPanel.vue'
import ReportsPanel from './components/ReportsPanel.vue'

</script>

<style>
body { font-family: system-ui, -apple-system, "Segoe UI", Roboto, "Helvetica Neue", Arial; background:#f7fafc; margin:0; padding:20px; color:#111; }
.container { max-width: 1100px; margin: 0 auto; }
header h1 { color:#2b7a78; margin:0 0 6px 0; }
main { display:grid; gap:16px; margin-top:12px; }
footer { margin-top:18px; color:#666; }
</style>
