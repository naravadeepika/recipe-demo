import axios from 'axios';

const API_URL = 'http://localhost:8080/api/recipes/paged';

class RecipeService {
    getRecipes(page = 1, limit = 10) {
        return axios.get(`${API_URL}?page=${page}&limit=${limit}&sortBy=rating`);
    }
}

export default new RecipeService();
