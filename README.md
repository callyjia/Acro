# Acro POS

A lightweight offline-first Point of Sale (POS) app for convenience store owners, built with Jetpack Compose.

## About
Acro helps small store owners manage daily operations without needing an internet connection. All data is stored locally on the device, making it suitable for low-end Android devices.

## Features

### Product Management
- Add, edit, and delete products
- View full product list (searchable, adjustable grid)
- **Capture a product photo with the camera** (CameraX + FileProvider, shown via Coil)
- **Scan a barcode to auto-fill the product's barcode** (CameraX + ML Kit)
- Track stock quantity — **out-of-stock products show in red**

### Transaction / Cart System
- Add products to cart (tap a product, or **scan its barcode to add it**)
- **Stock-aware ordering** — out-of-stock products are greyed out and cannot be added
- **Cart popup** — open from the cart icon (with item-count badge) above the total to review the order, change quantities, or remove lines
- Auto-calculate total price
- **Name an order** at checkout (blank → auto-named `order{id}`)
- Process checkout — saves the order **and every line item**
- Auto-deduct stock after purchase

### Order History & Detail
- List of all past orders with sorting (date / name / price)
- **Order Detail screen** — tap an order to see its name, ID, timestamp, and full item breakdown

### Analytics
- **Real sales analytics** derived from orders + product flow
- Total revenue, total orders, average / min / max order value
- **Revenue by period** — daily / weekly / monthly average, min, and max
- **Inventory stats** — product count, units in stock, out-of-stock count, total stock value

### Dashboard
- Home **sales card now shows live data** (today's revenue + today/week/month order counts)

### General
- Dark / light mode toggle (drawer)

## Tech Stack
- Kotlin
- Jetpack Compose
- Material 3
- Room Database (local storage)
- ViewModel + StateFlow
- CameraX + ML Kit Barcode Scanning
- Coil (image loading)

## Architecture
MVVM (Model - View - ViewModel)

- **database/** — Room entities, DAOs, repositories (Product + Transaction)
- **viewmodel/** — Product, Transaction, and Cart view models
- **analytics/** — pure (UI-free) sales-statistics computation
- **uiscreens/** — Compose screens and shared UI components
- **navigation/** — NavHost graph + bottom-bar/drawer items
- **ui/theme/** — color scheme, typography, dark-mode wrapper

## Getting Started
1. Clone the repo
2. Open in Android Studio
3. Run on a **physical device** (Android 8.0+) — the camera (barcode scan + photo capture) does not work reliably on emulators

## Current Progress
- [x] Navigation setup (NavHost + BottomBar + Drawer)
- [x] Home screen with task shortcuts + live sales card
- [x] Drawer menu + dark mode
- [x] Product management (add / edit / delete / list)
- [x] Camera photo capture for products
- [x] Barcode scanning (add-product + order screen)
- [x] Transaction/cart system (cart popup, stock-aware, custom order name)
- [x] Transaction history + order detail
- [x] Analytics (daily/weekly/monthly avg + min/max, inventory)
- [ ] QR payment screen (stub)
- [ ] Profile/account screen (stub)
- [ ] Settings & About screens (stub)
