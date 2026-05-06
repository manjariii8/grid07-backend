# 🚀 Grid07 Backend Assignment – Core API & Guardrails

## 📌 Overview

This project is a **Spring Boot microservice** that acts as a central API system with **strict guardrails using Redis** to control bot interactions, prevent abuse, and ensure system stability under high concurrency.

The system is designed to be:

* **Stateless**
* **Concurrency-safe**
* **Scalable**
* **Production-oriented**

---

## 🛠 Tech Stack

* **Java 17**
* **Spring Boot 3**
* **MySQL** (used instead of PostgreSQL)
* **Redis** (for atomic operations & caching)
* **Docker (Compose)**

---

## ⚙️ Setup Instructions

### 1. Clone Repository

```bash
git clone <your-repo-url>
cd grid07-backend
```

---

### 2. Start MySQL & Redis

```bash
docker-compose up -d
```

---

### 3. Run Application

Run the main class:

```
Grid07Application.java
```

---

### 4. Access APIs

Base URL:

```
http://localhost:8080
```

---

## 🌐 API Endpoints

### 📌 Create Post

```
POST /api/posts
```

### 💬 Add Comment

```
POST /api/posts/{postId}/comments
```

### ❤️ Like Post

```
POST /api/posts/{postId}/like
```

---

## 🔥 Redis Guardrails (Core Logic)

### 1. Virality Score (Real-Time)

Each interaction updates a Redis key:

| Interaction | Score |
| ----------- | ----- |
| Bot Reply   | +1    |
| Like        | +20   |
| Comment     | +50   |

Stored in:

```
post:{id}:virality_score
```

---

### 2. Atomic Locks (Concurrency Protection)

#### ✅ Horizontal Cap (Max 100 Bot Replies)

* Redis Key: `post:{id}:bot_count`
* Implemented using **atomic INCR operation**

✔ Guarantees:

* No more than 100 bot replies
* Prevents race conditions under concurrent requests

---

#### ✅ Vertical Cap (Thread Depth Limit)

* Maximum depth allowed: **20**
* Requests exceeding limit are rejected

---

#### ✅ Cooldown Cap (Bot-Human Interaction)

* A bot cannot interact with the same user within **10 minutes**
* Implemented using Redis key with **TTL**

Example:

```
cooldown:bot_{id}:human_{id}
```

---

## 🔔 Notification Engine (Smart Batching)

### 🔹 Redis Throttler

* If user already received notification:
  → Store in Redis List
* Else:
  → Send immediately & set cooldown

---

### 🔹 CRON Scheduler

* Runs every **5 minutes**
* Aggregates notifications
* Outputs:

```
Summarized Notification: X interactions
```

---

## 🧠 Thread Safety & Design Decisions

### 🔒 How Race Conditions Are Prevented

* Redis operations like `INCR` are **atomic**
* Ensures strict enforcement of limits (e.g., 100 bot replies)

---

### ⚙️ Stateless Architecture

* No in-memory storage (no HashMaps/static variables)
* All dynamic state stored in Redis

---

### 💾 Data Separation

* **MySQL → Source of truth (posts, comments)**
* **Redis → Guardrails + fast counters**

---

## 🧪 Testing

### ✔ Verified Features

* Virality score updates correctly
* Bot count enforced strictly
* Depth limit validated
* APIs tested using Postman

---

### 🔍 Sample Redis Keys

```
post:1:virality_score
post:1:bot_count
```

---

## ⚡ Race Condition Handling

Tested with multiple concurrent bot requests.

✔ Result:

* Bot replies strictly capped at **100**
* No overflow observed
* Redis atomic operations ensured correctness

---

## 🐳 Docker Configuration

### Services:

* MySQL
* Redis

---

### ⚠️ Port Note

If port **3306** is already in use:

* Change to **3307** in:

  * `docker-compose.yml`
  * `application.properties`

---

## 📦 Deliverables

* ✔ Spring Boot Source Code
* ✔ docker-compose.yml
* ✔ Postman Collection
* ✔ README Documentation

---

## 🎯 Conclusion

This project demonstrates:

* Strong backend fundamentals
* Understanding of distributed state management
* Practical use of Redis for concurrency control
* Clean and scalable system design

---

## 👤 Author

**Manjari Tripathi**
