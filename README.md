# Android Billing App

A simple Android billing application built to make day-to-day billing easier for a retail clothing store. The app supports creating bills, printing receipts over Bluetooth, and keeping track of daily sales.

I built this project using Kotlin and Jetpack Compose, with Room for local database storage. The main focus was to make billing quick and practical, especially when working with a Bluetooth thermal printer.

## Features

- **Create bills:** Generate bills for customer purchases directly from the app.
- **Live receipt preview:** View how a receipt will look before printing it.
- **Bluetooth thermal printing:** Connect to a compatible ESC/POS Bluetooth printer and print receipts.
- **Quick printer pairing:** Pair with a supported printer using a button, without going through a complicated setup process each time.
- **Daily sales records:** Save bills locally and review previous sales by date.
- **Cash and UPI tracking:** View daily sales totals separately for cash and UPI transactions.
- **Offline data storage:** Store billing records locally using Room, so the saved records remain available without an internet connection.

## Tech Stack

- **Kotlin** — Main programming language.
- **Jetpack Compose** — Building the app's UI with a declarative approach.
- **Room** — Local database for storing bills and related billing information.
- **Coroutines** — Handling asynchronous work without blocking the main thread.
- **StateFlow** — Observing and updating UI state.
- **Hybrid MVVM architecture** — Separating UI responsibilities from business logic while keeping the architecture practical for the application.
- **Repository pattern** — Providing a separate layer for database operations.
- **DantSu ESC/POS Thermal Printer Library** — Generating and printing receipts on compatible ESC/POS printers over Bluetooth.

## Architecture

The project follows a hybrid MVVM structure.

Jetpack Compose handles the UI, while ViewModels manage screen-related state and coordinate application operations. A repository layer handles local database access through Room.

This structure keeps database operations separate from the UI and makes the code easier to maintain as the application grows.

The general flow is:

`Jetpack Compose UI → ViewModel → Repository → Room Database`

Coroutines are used for asynchronous operations, and StateFlow helps the UI react to changes in application state.

## Bluetooth Receipt Printing

One of the main parts of this project is the thermal printer integration.

The app uses the DantSu ESC/POS library to prepare receipts for compatible thermal printers. It supports Bluetooth printer pairing, receipt preview, and printing directly from the billing workflow.

The goal was to keep the process simple enough for everyday shop use, where bills need to be generated and printed quickly.

## Daily Sales Management

The app also keeps a record of completed sales, allowing previous transactions to be reviewed by date.

Daily totals are separated by payment method:

- Cash
- UPI

This makes it easier to check how much the store sold on a particular day and how those sales were paid for.

## Data Storage

Room is used to store billing information locally. The database separates bill records and their associated information, helping the app organize and retrieve saved transactions.

Because the records are stored locally, the main billing and sales-history features do not depend on a constant internet connection.

## What I Learned

While working on this project, I gained practical experience with:

- Building Android interfaces using Jetpack Compose.
- Managing UI state with ViewModels and StateFlow.
- Structuring an application using a hybrid MVVM approach.
- Working with Room entities, relationships, and database operations.
- Using Kotlin Coroutines for asynchronous tasks.
- Integrating a third-party ESC/POS library.
- Working with Bluetooth thermal printers in a real billing workflow.
- Designing local sales records and calculating totals by date and payment method.

## Project

This project was developed for practical retail billing use and serves as part of my Android development portfolio.

**Built with Kotlin, Jetpack Compose, Room, Coroutines, and the DantSu ESC/POS library.**
