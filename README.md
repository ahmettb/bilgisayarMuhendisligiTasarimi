# Job Tracking Application - İş Takip Uygulaması

 **Kurumsal İş Süreçlerini Dijitalleştiren Mikroservis Tabanlı Çözüm**

Modern mikroservis mimarisi ve Spring Cloud teknolojileri kullanılarak geliştirilmiş, kurumsal düzeyde iş takip ve yönetim uygulaması. Farklı departmanların (İnsan Kaynakları, Pazarlama, Yazılım, Yönetim) iş süreçlerini merkezi bir platform üzerinden yönetmek, analiz etmek ve optimize etmek için tasarlanmıştır.


##  Sistem Mimarisi

Bu uygulama **Mikroservis Mimarisi** kullanılarak geliştirilmiştir:

![Sistem Mimarisi](blok.png)


### 🔧 Kullanılan Teknolojiler

- **Backend**: Java, Spring Boot, Spring Cloud
- **Veritabanı**: PostgreSQL
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Güvenlik**: Spring Security, JWT
- **ORM**: Spring Data JPA, Hibernate
- **Build Tool**: Maven
- **Logging**: Log4j2
- **Documentation**: Swagger/OpenAPI

##  Mikroservis Detayları

### 1. **Discovery Service** (Port: 9000)
- **Teknoloji**: Netflix Eureka Server
- **Görev**: Tüm mikroservislerin kayıt merkezi
- **Özellikler**: 
  - Servis keşfi ve kayıt
  - Servis durumu izleme
  - Eureka Dashboard
### 2. **API Gateway** (Port: 8500)
- **Teknoloji**: Spring Cloud Gateway
- **Görev**: Tüm isteklerin giriş noktası
- **Özellikler**:
  - JWT token doğrulama (AuthFilter)
  - Route yönetimi
  - Load balancing
  - CORS yönetimi

### 3. **Authentication Service** (Port: 9100)
- **Teknoloji**: Spring Security, JWT
- **Görev**: Kimlik doğrulama ve yetkilendirme
- **Endpoint'ler**:
  - `POST /api/auth/login` - Giriş yapma
  - `POST /api/auth/register` - Kayıt olma
  - `GET /api/auth/validate/{token}` - Token doğrulama
  - `GET /api/auth/get-user-info` - Kullanıcı bilgileri
  - `GET /api/auth/get-current-user` - Mevcut kullanıcı

### 4. **Admin Service** (Port: 8000)
- **Görev**: Merkezi yönetim ve koordinasyon
- **Özellikler**:
  - Diğer servislerle iletişim (RestTemplate Client)
  - Kullanıcı yönetimi
  - İzin onaylama/reddetme
  - Kampanya yönetimi
  - Görev analizi ve raporlama

### 5. **HR Service** (Port: 8001)
- **Görev**: İnsan Kaynakları süreçleri
- **Endpoint'ler**:
  - `POST /api/user/save` - Kullanıcı ekleme
  - `PUT /api/user/update/{id}` - Kullanıcı güncelleme
  - `DELETE /api/user/delete/{id}` - Kullanıcı silme
  - `GET /api/user/get/{id}` - Kullanıcı bilgisi
  - `GET /api/user/role/{role}` - Role göre kullanıcılar
  - `POST /api/hr/permission/save` - İzin talebi
  - `GET /api/hr/permission/get/{id}` - İzin bilgisi

### 6. **Marketing Service** (Port: 8002)
- **Görev**: Pazarlama kampanyaları yönetimi
- **Özellikler**: Redis cache entegrasyonu
- **Endpoint'ler**:
  - `POST /api/campaign/save` - Kampanya oluşturma
  - `GET /api/campaign/get/{id}` - Kampanya bilgisi
  - `GET /api/campaign/get-all` - Tüm kampanyalar
  - `PUT /api/campaign/update/{id}` - Kampanya güncelleme
  - `PUT /api/campaign/update-status` - Durum güncelleme
  - `DELETE /api/campaign/delete/{id}` - Kampanya silme

### 7. **Software Service** (Port: 8003)
- **Görev**: Yazılım projeleri ve görev yönetimi
- **Özellikler**:
  - Görev durumu takibi
  - Performans analizi ve raporlama
  - Yorum sistemi
- **Endpoint'ler**:
  - `POST /api/task/create` - Görev oluşturma
  - `PUT /api/task/update-status` - Görev durumu güncelleme
  - `GET /api/task/get/{id}` - Görev bilgisi
  - `GET /api/task/get-task-by-status` - Duruma göre görevler
  - `GET /api/task/get-task-by-assignee/{id}` - Atanan görevler
  - `GET /api/task/get-task-completion-rate/{userId}` - Tamamlanma oranı
  - `GET /api/task/get-task-status-analysis/{id}` - Durum analizi
  - `POST /api/comment/save` - Yorum ekleme

##  Kullanıcı Rolleri

- **ADMIN**: Sistem yöneticisi (tüm yetkiler)
- **HUMAN_RESOURCE**: İnsan Kaynakları personeli
- **MARKETING**: Pazarlama personeli
- **SOFTWARE**: Yazılım geliştirici

##  Kurulum ve Çalıştırma

### Gereksinimler
- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- Git

### Veritabanı Kurulumu
```sql
CREATE DATABASE job_tracking_db;
```

### Servisleri Çalıştırma Sırası

1. **Discovery Service** (Önce çalıştırılmalı):
```bash
cd discovery
mvn spring-boot:run
```

2. **Authentication Service**:
```bash
cd authentacition
mvn spring-boot:run
```

3. **API Gateway**:
```bash
cd api-gateway
mvn spring-boot:run
```

4. **Diğer servisler**:
```bash
# Admin Service
cd admin-service && mvn spring-boot:run

# HR Service  
cd hr-service && mvn spring-boot:run

# Marketing Service
cd marketing-service && mvn spring-boot:run

# Software Service
cd software-service && mvn spring-boot:run
```

##  Monitoring ve Dashboard

- **Eureka Dashboard**: http://localhost:9000
- **Gateway Health**: http://localhost:8500/actuator/health
- **Service Health**: Her servis için `/actuator/health`

##  Konfigürasyon

Her servisin `application.yml` dosyasında:
- Veritabanı bağlantısı (PostgreSQL)
- Eureka server adresi
- Port numaraları
- JWT secret key


##  Özellikler

- **Kullanıcı Yönetimi**: CRUD işlemleri, rol tabanlı erişim
- **Görev Takibi**: Durum yönetimi, atama, analiz
- **Kampanya Yönetimi**: Oluşturma, güncelleme, durum takibi
- **İzin Sistemi**: Talep, onay/red süreçleri
- **Analitik**: Görev tamamlanma oranları, durum analizleri

---
