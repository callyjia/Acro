# Acro POS

A lightweight offline-first Point of Sale (POS) app for convenience store owners, built with Jetpack Compose.

## About
Acro helps small store owners manage daily operations without needing an internet connection. All data is stored locally on the device, making it suitable for low-end Android devices.

## Features

### Product Management
- Add, edit, and delete products
- View full product list
- Store product image
- Track stock quantity

### Transaction System
- Add products to cart
- Auto-calculate total price
- Process checkout
- Save transaction history
- Auto-deduct stock after purchase

## Tech Stack
- Kotlin
- Jetpack Compose
- Material 3
- Room Database (local storage)
- ViewModel + StateFlow

## Architecture
MVVM (Model - View - ViewModel)

## Getting Started
1. Clone the repo
2. Open in Android Studio
3. Run on emulator or physical device (Android 8.0+)

## Current Progress
- [x] Navigation setup (NavHost + BottomBar + Drawer)
- [x] Home screen with task shortcuts
- [x] Drawer menu
- [ ] Product management
- [ ] Transaction/cart system
- [ ] Transaction history
- [ ] Analytics
