import React from 'react';
import RecipeService from '../service/RecipeService';
import Rating from 'react-rating-stars-component';
import { Drawer, Collapse } from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ExpandLessIcon from '@mui/icons-material/ExpandLess';

class RecipeTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            recipes: [],
            page: 1,
            limit: 15,
            total: 0,
            selectedRecipe: null,
            expandTime: false
        };
    }

    componentDidMount() {
        this.fetchRecipes();
    }

    fetchRecipes = () => {
        const { page, limit } = this.state;
        RecipeService.getRecipes(page, limit)
            .then(res => {
                const data = res.data.data.map(recipe => ({
                    ...recipe,
                    nutrients: recipe.nutrients ? JSON.parse(recipe.nutrients) : null
                }));
                this.setState({ recipes: data, total: res.data.total });
            })
            .catch(err => console.error('API error:', err));
    };

    handlePageChange = (delta) => {
        this.setState(prev => ({ page: prev.page + delta }), this.fetchRecipes);
    };

    toggleDrawer = (recipe) => {
        this.setState(prev => ({
            selectedRecipe: prev.selectedRecipe ? null : recipe,
            expandTime: false
        }));
    };

    toggleExpandTime = () => {
        this.setState(prev => ({ expandTime: !prev.expandTime }));
    };

    render() {
        const { recipes, page, limit, total, selectedRecipe, expandTime } = this.state;

        return (
            <div className="p-4">
                <h2 className="text-2xl font-bold mb-4">Recipe List</h2>

                <table className="table-auto border-collapse border border-gray-300 w-full">
                    <thead>
                        <tr>
                            <th className="border px-2">Title</th>
                            <th className="border px-2">Cuisine</th>
                            <th className="border px-2">Rating</th>
                            <th className="border px-2">Total Time</th>
                            <th className="border px-2">Serves</th>
                        </tr>
                    </thead>
                    <tbody>
                        {recipes.map(recipe => (
                            <tr key={recipe.idRecipeFood} onClick={() => this.toggleDrawer(recipe)} style={{ cursor: 'pointer' }}>
                                <td className="border px-2">{recipe.title.length > 30 ? recipe.title.substring(0,30)+'...' : recipe.title}</td>
                                <td className="border px-2">{recipe.cuisine}</td>
                                <td className="border px-2"><Rating value={recipe.rating || 0} size={20} edit={false} /></td>
                                <td className="border px-2">{recipe.totalTime || '-'}</td>
                                <td className="border px-2">{recipe.serves || '-'}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>

                {/* Pagination */}
                <div className="mt-4 flex justify-between items-center">
                    <button disabled={page===1} onClick={() => this.handlePageChange(-1)} className="px-3 py-1 bg-gray-200 rounded">Prev</button>
                    <span>Page {page}</span>
                    <button disabled={page*limit>=total} onClick={() => this.handlePageChange(1)} className="px-3 py-1 bg-gray-200 rounded">Next</button>
                </div>

                {/* Drawer */}
                <Drawer anchor="right" open={!!selectedRecipe} onClose={() => this.toggleDrawer(null)}>
                    {selectedRecipe && (
                        <div className="p-6 w-96">
                            <h2 className="text-xl font-bold">{selectedRecipe.title}</h2>
                            <p className="text-gray-600 mb-2">{selectedRecipe.cuisine}</p>
                            <p className="mb-2"><strong>Description:</strong> {selectedRecipe.description || '-'}</p>
                            <p className="mb-2"><strong>Total Time:</strong> {selectedRecipe.totalTime || '-'} mins</p>

                            {/* Expandable Prep/Cook Time */}
                            <div>
                                <div className="flex items-center justify-between cursor-pointer" onClick={this.toggleExpandTime}>
                                    <span className="font-medium">Prep & Cook Time</span>
                                    {expandTime ? <ExpandLessIcon /> : <ExpandMoreIcon />}
                                </div>
                                <Collapse in={expandTime}>
                                    <p>Prep Time: {selectedRecipe.prepTime || '-'} mins</p>
                                    <p>Cook Time: {selectedRecipe.cookTime || '-'} mins</p>
                                </Collapse>
                            </div>

                            {/* Nutrients Table */}
                            {selectedRecipe.nutrients && (
                                <div className="mt-4">
                                    <h3 className="font-semibold mb-2">Nutrients</h3>
                                    <table className="w-full text-left border">
                                        <thead>
                                            <tr>
                                                {Object.keys(selectedRecipe.nutrients).map(key => <th key={key} className="border px-2">{key}</th>)}
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                {Object.values(selectedRecipe.nutrients).map((val,i) => <td key={i} className="border px-2">{val}</td>)}
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            )}
                        </div>
                    )}
                </Drawer>

                {recipes.length===0 && <div className="mt-4 text-center text-gray-500">No results found</div>}
            </div>
        );
    }
}

export default RecipeTable;
