# Home Page API Endpoints

This document lists all API endpoints used for the home page functionality.

## Base URL
All endpoints are relative to the base API URL (configured in your app constants).

---

## 🏦 Bank Accounts & Cards

### 1. List Bank Cards
**Endpoint:** `api/BankAccounts/ListBankCards`  
**Method:** `POST`  
**Purpose:** Get all bank cards for the authenticated user  
**Used in:** Home page to display credit/debit cards  
**Repository:** `BankAccountsRepository.getBankCards()`  
**BLoC Event:** `LoadBankCardsEvent`

**Request Body:**
```json
{
  "RequestInfo": {
    "SaltSignature": "session_key_here",
    "Username": null,
    "PasswordHash": null
  }
}
```

**Response:** `BankCardsResponse` containing list of `BankCard` objects

---

### 2. List Bank Accounts
**Endpoint:** `api/BankAccounts/ListBankAccounts`  
**Method:** `POST`  
**Purpose:** Get all bank accounts for the authenticated user  
**Used in:** Home page to display account cards  
**Repository:** `BankAccountsRepository.getBankAccounts()`  
**BLoC Event:** `LoadBankAccountsEvent`

**Request Body:**
```json
{
  "RequestInfo": {
    "SaltSignature": "session_key_here",
    "Username": null,
    "PasswordHash": null
  }
}
```

**Response:** `BankAccountsResponse` containing list of `BankAccount` objects

---

### 3. List Bank Loans
**Endpoint:** `api/BankAccounts/ListBankLoans`  
**Method:** `POST`  
**Purpose:** Get all bank loans for the authenticated user  
**Repository:** `BankAccountsRepository.getBankLoans()`

**Request Body:**
```json
{
  "RequestInfo": {
    "SaltSignature": "session_key_here"
  }
}
```

---

### 4. List Bank Deposits
**Endpoint:** `api/BankAccounts/ListBankDeposits`  
**Method:** `POST`  
**Purpose:** Get all bank deposits for the authenticated user  
**Repository:** `BankAccountsRepository.getBankDeposits()`

**Request Body:**
```json
{
  "RequestInfo": {
    "SaltSignature": "session_key_here"
  }
}
```

---

## 💳 Operations & Transactions

### 5. List Bank Card Operations (Transaction History)
**Endpoint:** `api/OperationsBankCards/ListBankCardOperations`  
**Method:** `POST`  
**Purpose:** Get transaction history for bank cards  
**Used in:** Home page transactions section  
**Note:** This endpoint should be used to fetch the transaction list shown on the home page

**Request Body:**
```json
{
  "RequestInfo": {
    "SaltSignature": "session_key_here"
  },
  "CardId": "card_id_here",
  "StartDate": "2026-01-01",
  "EndDate": "2026-01-31"
}
```

---

### 6. List Bank Card Statements
**Endpoint:** `api/OperationsBankCards/ListBankCardStatement`  
**Method:** `POST`  
**Purpose:** Get detailed card statements  
**Used in:** Card statements view

**Request Body:**
```json
{
  "RequestInfo": {
    "SaltSignature": "session_key_here"
  },
  "CardId": "card_id_here"
}
```

---

### 7. Card to Card Operation
**Endpoint:** `api/OperationsBankCards/OperationCardToCard`  
**Method:** `POST`  
**Purpose:** Transfer money from one card to another  
**Used in:** Transfer functionality

---

### 8. Card to Account Operation
**Endpoint:** `api/OperationsBankCards/OperationCardToAccount`  
**Method:** `POST`  
**Purpose:** Transfer money from card to account  
**Used in:** Transfer functionality

---

## 👤 User Data

### 9. Get Mobile User Data
**Endpoint:** `api/MobileUser/GetMobileUserData`  
**Method:** `POST`  
**Purpose:** Get user profile information (name, avatar, etc.)  
**Used in:** Home page top section (user name and avatar)

**Request Body:**
```json
{
  "RequestInfo": {
    "SaltSignature": "session_key_here"
  },
  "SignInType": 1,
  "MobileNumber": "phone_number",
  "MobileNumberSecretCode": "secret_code"
}
```

**Response:** Contains user profile data including name, avatar URL, etc.

---

## 🔔 Notifications

### 10. List User Notifications
**Endpoint:** `api/BankNews/ListUserNotifications`  
**Method:** `POST`  
**Purpose:** Get user notifications  
**Used in:** Home page notification bell icon

**Request Body:**
```json
{
  "RequestInfo": {
    "SaltSignature": "session_key_here"
  }
}
```

---

## 💱 Exchange Rates

### 11. List Bank Exchange Rates
**Endpoint:** `api/BankNews/ListBankExchangeRates`  
**Method:** `POST`  
**Purpose:** Get current exchange rates  
**Used in:** May be displayed on home page or used for currency conversions

**Request Body:**
```json
{
  "RequestInfo": {
    "SaltSignature": "session_key_here"
  }
}
```

---

## 📊 Home Page Data Flow

### Current Implementation (HomeBloc)
The home page currently loads data using:

1. **RefreshHomeDataEvent** → Triggers both:
   - `getBankCards()` → `api/BankAccounts/ListBankCards`
   - `getBankAccounts()` → `api/BankAccounts/ListBankAccounts`

2. **LoadBankCardsEvent** → `api/BankAccounts/ListBankCards`
3. **LoadBankAccountsEvent** → `api/BankAccounts/ListBankAccounts`

### Recommended Additional Endpoints for Full Home Page

To fully implement the home page as per Figma design, you should also call:

1. **User Profile Data:**
   - `api/MobileUser/GetMobileUserData` - For user name and avatar

2. **Transaction History:**
   - `api/OperationsBankCards/ListBankCardOperations` - For transactions list
   - May need to call for each card or aggregate all transactions

3. **Notifications:**
   - `api/BankNews/ListUserNotifications` - For notification count/badge

4. **Total Balance Calculation:**
   - Calculate from `ListBankCards` and `ListBankAccounts` responses
   - Sum all card balances and account balances

---

## 🔐 Authentication

All endpoints require authentication via `RequestInfo`:
- **SaltSignature:** Session key (from successful sign-in)
- **Username:** Should be null for authenticated requests
- **PasswordHash:** Should be null for authenticated requests

Use `RequestBuilder.getCommonRequest()` to generate the correct request structure.

---

## 📝 Notes

1. **Balance Calculation:** The total balance shown on home page should be calculated by:
   - Summing all `cardBalance` from `BankCard` objects
   - Summing all `balanceInLC` from `BankAccount` objects
   - Converting to base currency if needed

2. **Transactions:** Currently, there's no direct endpoint for "all transactions". You may need to:
   - Call `ListBankCardOperations` for each card
   - Aggregate and sort by date
   - Or check if there's a unified transactions endpoint

3. **User Avatar:** The user avatar URL should come from `GetMobileUserData` response

4. **Real-time Updates:** Consider implementing pull-to-refresh or periodic updates for:
   - Balance updates
   - New transactions
   - Notification count

---

## 🚀 Implementation Example

```dart
// In HomeBloc or HomePageScreen
Future<void> loadHomeData() async {
  final requestInfo = await _requestBuilder.getCommonRequest();
  
  // Load cards
  final cardsResponse = await _bankAccountsRepository.getBankCards(
    requestInfo: requestInfo,
  );
  
  // Load accounts
  final accountsResponse = await _bankAccountsRepository.getBankAccounts(
    requestInfo: requestInfo,
  );
  
  // Load user data
  final userDataResponse = await _apiService.getMobileUserData({
    'RequestInfo': requestInfo.toJson(),
    // ... other required fields
  });
  
  // Load transactions (for each card or aggregated)
  final transactionsResponse = await _apiService.listBankCardOperations({
    'RequestInfo': requestInfo.toJson(),
    'CardId': cardId,
    'StartDate': startDate,
    'EndDate': endDate,
  });
  
  // Calculate total balance
  final totalBalance = calculateTotalBalance(
    cardsResponse.bankCards,
    accountsResponse.bankAccounts,
  );
}
```

---

## 📍 File Locations

- **Endpoints Definition:** `lib/core/constants/api_endpoints.dart`
- **API Service:** `lib/data/datasources/remote/api_service.dart`
- **Bank Accounts Repository:** `lib/data/repositories/bank_accounts_repository.dart`
- **Home BLoC:** `lib/presentation/home/bloc/home_bloc.dart`
- **Home Screen:** `lib/presentation/home/screens/home/home_page_screen.dart`
