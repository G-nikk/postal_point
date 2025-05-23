import React, { useState, useEffect } from 'react';
import { newspaperAPI } from '../services/api';
import NewspaperForm from './NewspaperForm';
import NewspaperDetails from './NewspaperDetails';
import StatisticsPanel from './StatisticsPanel';
import {
    Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    Paper, Button, IconButton, Dialog, CircularProgress, Chip,
    Typography, Grid
} from '@mui/material';
import { Edit, Delete, Info, LocalPostOffice, Print } from '@mui/icons-material';

export default function NewspaperList() {
    const [newspapers, setNewspapers] = useState([]);
    const [selectedNewspaper, setSelectedNewspaper] = useState(null);
    const [openForm, setOpenForm] = useState(false);
    const [loading, setLoading] = useState(true);
    const [stats, setStats] = useState({
        totalCost: null,
        editorInfo: null,
        printingHouses: []
    });

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        try {
            const { data } = await newspaperAPI.getAll();
            setNewspapers(data);
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        try {
            await newspaperAPI.delete(id);
            loadData();
        } catch (error) {
            console.error('Delete error:', error.response?.data);
        }
    };

    const loadStatistics = async (newspaper) => {
        try {
            const [printingHouses, totalCost] = await Promise.all([
                newspaperAPI.getPrintHouses(newspaper.newspaperID),
                newspaperAPI.getTotalCost(newspaper.newspaperID)
            ]);

            setStats({
                printingHouses: printingHouses.data,
                totalCost: totalCost.data,
                editorInfo: null
            });
        } catch (error) {
            console.error('Statistics load error:', error);
        }
    };

    if (loading) return <CircularProgress sx={{ mt: 4 }} />;

    return (
        <div className="container">
            <Typography variant="h4" gutterBottom>
                Newspaper Management System
            </Typography>

            <Grid container spacing={3}>
                <Grid item xs={12} md={8}>
                    <Button
                        variant="contained"
                        startIcon={<Print />}
                        onClick={() => {
                            setSelectedNewspaper(null); // Сброс выбранной газеты
                            setOpenForm(true); // Открытие формы для создания
                        }}
                        sx={{ mb: 2 }}
                    >
                        Add New Newspaper
                    </Button>

                    <TableContainer component={Paper}>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>Name</TableCell>
                                    <TableCell>Index</TableCell>
                                    <TableCell>Editor</TableCell>
                                    <TableCell>Price</TableCell>
                                    <TableCell>Actions</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {newspapers.map((paper) => (
                                    <TableRow key={paper.newspaperID}>
                                        <TableCell>{paper.name}</TableCell>
                                        <TableCell>{paper.indexEdition}</TableCell>
                                        <TableCell>{paper.editor}</TableCell>
                                        <TableCell>${paper.price?.toFixed(2)}</TableCell>
                                        <TableCell>
                                            <IconButton
                                                onClick={() => {
                                                    setSelectedNewspaper(paper);
                                                    setOpenForm(true); // Редактирование
                                                }}
                                            >
                                                <Edit color="primary" />
                                            </IconButton>
                                            <IconButton
                                                onClick={() => {
                                                    setSelectedNewspaper(paper);
                                                    loadStatistics(paper);
                                                }}
                                            >
                                                <Info color="info" />
                                            </IconButton>
                                            <IconButton onClick={() => handleDelete(paper.newspaperID)}>
                                                <Delete color="error" />
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Grid>

                {/* ... остальной код без изменений ... */}
            </Grid>

            <Dialog open={openForm} onClose={() => setOpenForm(false)} maxWidth="md">
                <NewspaperForm
                    newspaper={selectedNewspaper}
                    onClose={() => {
                        setOpenForm(false);
                        setSelectedNewspaper(null);
                        loadData();
                    }}
                />
            </Dialog>

            {selectedNewspaper && (
                <NewspaperDetails
                    newspaper={selectedNewspaper}
                    onClose={() => setSelectedNewspaper(null)}
                />
            )}
        </div>
    );
}