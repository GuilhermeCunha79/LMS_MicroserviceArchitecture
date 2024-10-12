# Technical Memo 1.2

## Problem:
**Adopting different IAM (Identity and Access Management) providers** (e.g. Google, Facebook, Azure).

## Summary of Solutions:
Adopt the following tactics:
- **Reduce coupling** namely:
    - Use intermediary services to manage multiple providers;
    - Abstract common services for authentication and user management;
    - Defer binding to configuration at runtime to allow for flexibility across environments and providers.

## Solution:
1. **Use intermediary:**
    - Implement an **OAuth 2.0 flow** that acts as an intermediary between the system and the external IAM providers.
    - Use a library or service that abstracts the differences between providers (e.g., Passport.js, Spring Security OAuth).

2. **Use abstract common services:**
    - Define a common interface or service to manage user authentication and authorization, abstracting the differences between each IAM provider (e.g., handling tokens, session management, user profiles).
    - This ensures that core application logic remains decoupled from specific IAM providers.

3. **Defer binding:**
    - Address configuration by deferring the selection of the specific IAM provider to **runtime configuration**.
    - Use environment-specific configurations (e.g., `.env` files, or Spring profiles) to select the appropriate IAM provider dynamically (Google, Facebook, Azure).

## Motivation:
- Support multiple Identity and Access Management providers while maintaining a **single codebase**.
- Reduce the complexity and avoid vendor lock-in by keeping the business logic decoupled from specific IAM implementations.
- Ensure that different clients or environments can use different IAM providers according to their constraints or preferences, without requiring significant changes to the core codebase.

## Pending Issues:
1. **Refactoring Complexity:**
    - How difficult and long will it take to refactor the current solution to adopt other IAM providers (e.g., from Google to Azure)?
    - Need to analyze the current codebase to assess how tightly coupled it is to a single provider.

2. **System Modifications:**
    - What parts of the system need to be modified?
    - Areas to consider: authentication flow, token management, user session management, and error handling for provider-specific issues.

3. **Supported IAM Providers:**
    - What Identity Providers should be supported?
    - According to the Product Owner, the system must support **Google, Facebook**, and **Azure**.

---

## Conclusion:
Adopting multiple IAM providers using this approach allows the system to maintain flexibility, scalability, and security while supporting different authentication workflows based on the provider. By abstracting common functionality and deferring provider selection to configuration, the system remains decoupled from specific implementations, making future integrations or provider swaps easier.
