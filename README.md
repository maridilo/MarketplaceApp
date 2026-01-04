https://github.com/maridilo/MarketplaceApp.git


https://gamma.app/docs/Marketplace-App-Prueba-de-Concepto-POC-a1mcb1yqrxqvcyb
# 🛒 Marketplace App - Android

Aplicación nativa de comercio electrónico desarrollada como Proyecto Final para la asignatura de **Programación Dirigida por Eventos**.

La app implementa una arquitectura profesional **MVVM** y combina persistencia de datos local y en la nube.

## 👥 Autores
* **Maria Diaz – Heredero Lopez**
* **Cintia Santillan Garcia**

## 📱 Funcionalidades Principales
1.  **Autenticación de Usuarios:** Registro e inicio de sesión seguro mediante **Firebase Auth**.
2.  **Catálogo de Productos:** Visualización en Grid con carga asíncrona de imágenes.
3.  **Buscador en Tiempo Real:** Filtrado instantáneo de productos por nombre.
4.  **Carrito de Compra:** Persistencia local usando **Room Database**. Los datos se mantienen al cerrar la app.
5.  **Gestión de Pedidos:** Historial de compras y simulación de checkout.
6.  **Perfil de Usuario:** Gestión de sesión y datos del usuario.

## 🛠️ Tecnologías y Arquitectura

El proyecto sigue el patrón de arquitectura **MVVM (Model-View-ViewModel)** con **Repository Pattern**.

* **Lenguaje:** Java ☕
* **Base de Datos Local:** Room (SQLite abstraction layer).
* **Backend / Nube:** Firebase Authentication.
* **Imágenes:** Glide (librería de carga y caché).
* **Componentes de Arquitectura:**
    * `LiveData` & `ViewModel` (Datos reactivos).
    * `ExecutorService` (Manejo de hilos en segundo plano).
    * `RecyclerView` & `GridLayoutManager`.

## 🚀 Instalación

1. Clonar el repositorio.
2. Abrir en **Android Studio**.
3. Sincronizar Gradle (`File -> Sync Project with Gradle Files`).
4. Ejecutar en un emulador o dispositivo físico.
