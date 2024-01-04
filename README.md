## Reference
- https://github.com/ali-bouali/spring-boot-3-jwt-security
- https://github.com/dkstm95/spring-boot-3-jwt-template

## API
### 회원가입
- URL: `/api/v1/users`
- Method: `POST`
- Request
```json
{
  "loginId": "test1234",
  "password": "test1234",
  "name": "test",
  "role": "USER"
}
```
- Response
```json
{
  "loginId": "test1234"
}
```

<br>

### 로그인
- URL: `/api/v1/login`
- Method: `POST`
- Request
```json
{
  "loginId": "test1234",
  "password": "test1234"
}
```
- Response
```json
{
  "accessToken": "..",
  "refreshToken": ".."
}
```

<br>

### 로그아웃
- URL: `/api/v1/auth/logout`
- Method: `POST`
- Header
```
Authorization: Bearer {accessToken}
```

<br>

### Refresh Token
- URL: `/api/v1/auth/refresh-token`
- Method: `POST`
- Request
```json
{
  "refreshToken": ".."
}
```
- Response
```json
{
  "accessToken": "..",
  "refreshToken": ".."
}
```

<br>

### 회원 목록 (ADMIN 권한)
- URL: `/api/v1/users`
- Method: `GET`
- Header
```
Authorization: Bearer {accessToken}
```
- Response
```json
{
  "users": [

  ]
}
```

<br>

### 회원 정보
- URL: `/api/v1/users/{loginId}`
- Method: `GET`
- Header
```
Authorization: Bearer {accessToken}
```
- Response
```json
{
  "loginId": "test1234",
  "name": "test"
}
```

<br>

### 회원 정보 수정
- URL: `/api/v1/users`
- Method: `PUT`
- Header
```
Authorization: Bearer {accessToken}
```
- Request
```json
{
  "name": "test"
}
```
- Response
```json
{
  "loginId": "test1234",
  "name": "test"
}
```

<br>

### 회원 탈퇴
- URL: `/api/v1/users`
- Method: `DELETE`
- Header
```
Authorization: Bearer {accessToken}
```