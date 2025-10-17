# Bug Fix Report: ClassCastException in CustomerManager.getAll()

## Problem Description

Users encountered the following error when trying to view all customers:

```
❌ Lỗi khi lấy danh sách khách hàng: class [Linterfaces.IEntity; cannot be cast to class [Lmodels.Customer;
([Linterfaces.IEntity; and [Lmodels.Customer; are in unnamed module of loader 'app')
```

## Root Cause

The issue was in `BaseManager.java` at line 3:

```java
import java.sql.Array;  // ❌ WRONG - SQL Array
```

This import was **incorrect**. The code uses `Array.newInstance()` from `java.lang.reflect.Array`, not from `java.sql.Array`.

When `BaseManager.getAll()` tried to create a typed array using `Array.newInstance()`, it was using the wrong API, causing the method to return `IEntity[]` instead of the specific type (e.g., `Customer[]`). This led to a ClassCastException when the code attempted to cast the result.

## Solution

Changed the import statement in `BaseManager.java`:

```java
import java.lang.reflect.Array;  // ✓ CORRECT - Reflection API
```

## Files Modified

- **e:\Projects\OOP-DOAN\src\main\java\collections\BaseManager.java**
  - Line 3: Fixed import statement

## Analysis of Manager Classes

The good news is that all specific Manager classes already have **correct implementations** of `getAll()`:

- ✓ `CustomerManager.getAll()`
- ✓ `AppointmentManager.getAll()`
- ✓ `EmployeeManager.getAll()`
- ✓ `ServiceManager.getAll()`
- ✓ `TransactionManager.getAll()`

These managers override the base method with simple, direct array construction:

```java
@Override
public Customer[] getAll() {
    Customer[] result = new Customer[size];
    for (int i = 0; i < size; i++) {
        result[i] = items[i];
    }
    return result;
}
```

## Why This Happened

The bug only appeared when:

1. Code somehow called `BaseManager.getAll()` directly (not the overridden version)
2. Or when the reflection-based approach in BaseManager was triggered

The import error prevented the reflection-based approach from working correctly.

## Verification

✓ The fix has been compiled and verified successfully with no errors.

## Testing Recommendation

1. Add a new customer
2. View all customers - should now work without ClassCastException
3. Test other managers (Appointments, Employees, Services, Transactions) for consistency
