import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api', // Spring Boot runs on 8080
});

export default api;
