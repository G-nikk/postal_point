import React, { useState, useEffect } from 'react';
import { printingHouseAPI } from '../services/api';
import {
    Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    Paper, Button, IconButton, Dialog, TextField, Typography, Grid,
    CircularProgress, Accordion, AccordionSummary, AccordionDetails
} from '@mui/material';
import { Add, Edit, Delete, Info, FilterList } from '@mui/icons-material';

function PrintingHouseForm({ printingHouse, onClose, onSave }) {
    const [formData, setFormData] = useState({
        name: printingHouse?.name || '',
        address: printingHouse?.address || ''
    });

    const handleSubmit = () => {
        onSave(formData);
        onClose();
    };

    return (
        <Dialog open onClose={onClose} maxWidth="sm" fullWidth>
            <DialogTitle>{printingHouse ? 'Редактировать' : 'Новая'} типография</DialogTitle>
            <DialogContent>
                <Grid container spacing={2} sx={{ mt: 1 }}>
                    <Grid item xs={12}>
                        <TextField
                            fullWidth
                            label="Название"
                            value={formData.name}
                            onChange={(e) => setFormData({...formData, name: e.target.value})}
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            fullWidth
                            label="Адрес"
                            value={formData.address}
                            onChange={(e) => setFormData({...formData, address: e.target.value})}
                        />
                    </Grid>
                </Grid>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Отмена</Button>
                <Button onClick={handleSubmit} variant="contained" color="primary">
                    Сохранить
                </Button>
            </DialogActions>
        </Dialog>
    );
}

export default function PrintingHouseList() {
    const [printingHouses, setPrintingHouses] = useState([]);
    const [selectedPH, setSelectedPH] = useState(null);
    const [openForm, setOpenForm] = useState(false);
    const [filters, setFilters] = useState({ name: '', address: '' });
    const [details, setDetails] = useState({ newspapers: [], maxEditor: '' });

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        const { data } = await printingHouseAPI.getAll();
        setPrintingHouses(data);
    };

    const handleSave = async (data) => {
        if (selectedPH) {
            await printingHouseAPI.update(selectedPH.printingHouseID, data);
        } else {
            await printingHouseAPI.create(data);
        }
        loadData();
    };

    const handleDelete = async (id) => {
        await printingHouseAPI.delete(id);
        loadData();
    };

    const loadDetails = async (id) => {
        const [newspapersRes, editorRes] = await Promise.all([
            printingHouseAPI.getNewspapers(id),
            printingHouseAPI.getMaxEditor(id)
        ]);
        setDetails({
            newspapers: newspapersRes.data,
            maxEditor: editorRes.data
        });
    };

    const filteredData = printingHouses.filter(ph => {
        return ph.name.toLowerCase().includes(filters.name.toLowerCase()) &&
            ph.address.toLowerCase().includes(filters.address.toLowerCase());
    });

    return (
        <div className="container">
            <Typography variant="h4" gutterBottom>
                Управление типографиями
            </Typography>

            <Grid container spacing={3}>
                <Grid item xs={12}>
                    <Button
                        variant="contained"
                        startIcon={<Add />}
                        onClick={() => {
                            setSelectedPH(null);
                            setOpenForm(true);
                        }}
                        sx={{ mb: 2 }}
                    >
                        Добавить типографию
                    </Button>

                    <Accordion>
                        <AccordionSummary expandIcon={<FilterList />}>
                            <Typography>Фильтры</Typography>
                        </AccordionSummary>
                        <AccordionDetails>
                            <Grid container spacing={2}>
                                <Grid item xs={12} md={6}>
                                    <TextField
                                        fullWidth
                                        label="Поиск по названию"
                                        value={filters.name}
                                        onChange={(e) => setFilters({...filters, name: e.target.value})}
                                    />
                                </Grid>
                                <Grid item xs={12} md={6}>
                                    <TextField
                                        fullWidth
                                        label="Поиск по адресу"
                                        value={filters.address}
                                        onChange={(e) => setFilters({...filters, address: e.target.value})}
                                    />
                                </Grid>
                            </Grid>
                        </AccordionDetails>
                    </Accordion>

                    <TableContainer component={Paper} sx={{ mt: 2 }}>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>Название</TableCell>
                                    <TableCell>Адрес</TableCell>
                                    <TableCell>Действия</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {filteredData.map(ph => (
                                    <TableRow key={ph.printingHouseID}>
                                        <TableCell>{ph.name}</TableCell>
                                        <TableCell>{ph.address}</TableCell>
                                        <TableCell>
                                            <IconButton onClick={() => {
                                                setSelectedPH(ph);
                                                loadDetails(ph.printingHouseID);
                                            }}>
                                                <Info color="info" />
                                            </IconButton>
                                            <IconButton onClick={() => {
                                                setSelectedPH(ph);
                                                setOpenForm(true);
                                            }}>
                                                <Edit color="primary" />
                                            </IconButton>
                                            <IconButton onClick={() => handleDelete(ph.printingHouseID)}>
                                                <Delete color="error" />
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Grid>

                {selectedPH && (
                    <Grid item xs={12}>
                        <Accordion defaultExpanded>
                            <AccordionSummary>
                                <Typography>Детали типографии: {selectedPH.name}</Typography>
                            </AccordionSummary>
                            <AccordionDetails>
                                <Typography variant="h6" gutterBottom>
                                    Газеты и тиражи:
                                </Typography>
                                <List>
                                    {details.newspapers.map(np => (
                                        <ListItem key={np.newspaperID}>
                                            <ListItemText
                                                primary={np.newspaper.name}
                                                secondary={`Тираж: ${np.quantity} экз.`}
                                            />
                                        </ListItem>
                                    ))}
                                </List>

                                <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
                                    Редактор газеты с максимальным тиражом:
                                </Typography>
                                <Typography>{details.maxEditor || 'Нет данных'}</Typography>
                            </AccordionDetails>
                        </Accordion>
                    </Grid>
                )}
            </Grid>

            {openForm && (
                <PrintingHouseForm
                    printingHouse={selectedPH}
                    onClose={() => {
                        setOpenForm(false);
                        setSelectedPH(null);
                    }}
                    onSave={handleSave}
                />
            )}
        </div>
    );
}