<template>
  <div>
    <h2>Склад</h2>

    <h3>Добавить товар</h3>
    <form @submit.prevent="addProduct">
      <input
        v-model="newProduct.name"
        placeholder="Название товара"
        required
      />
      <input
        v-model.number="newProduct.quantity"
        type="number"
        placeholder="Начальное количество"
        required
        min="0"
      />
      <button type="submit">Добавить</button>
    </form>

    <h3>Товары на складе</h3>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Название</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="product in products" :key="product.id">
          <td>{{ product.id }}</td>
          <td>{{ product.name }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      products: [],
      newProduct: {
        name: '',
        quantity: 0
      },
      timer: null
    }
  },

  mounted() {
    this.fetchProducts();

    // 🔁 автообновление склада
    this.timer = setInterval(() => {
      this.fetchProducts();
    }, 3000);
  },

  beforeUnmount() {
    clearInterval(this.timer);
  },

  methods: {
    async fetchProducts() {
      const res = await fetch('http://localhost:8080/inventory');
      if (res.ok) {
        this.products = await res.json();
      }
    },

    async addProduct() {
      await fetch('http://localhost:8080/inventory', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(this.newProduct)
      });

      this.newProduct = { name: '', quantity: 0 };
      await this.fetchProducts();
    }
  }
}
</script>

<style>
table {
  border-collapse: collapse;
  width: 100%;
}

th, td {
  border: 1px solid #ccc;
  padding: 8px;
}
</style>

