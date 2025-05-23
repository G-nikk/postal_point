import React, { useState, useEffect } from 'react';
import { postOfficeAPI } from '../services/api';
import {
    Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    Paper, Button, IconButton, Dialog, DialogTitle, DialogContent,
    DialogActions, TextField, Typography, Grid, CircularProgress,
    Accordion, AccordionSummary, AccordionDetails, List, ListItem,
    ListItemText, Chip
} from '@mui/material';
import { Add, Edit, Delete, Info, FilterList } from '@mui/icons-material';

function PostOfficeForm({ postOffice, onClose, onSave }) {
    const [formData, setFormData] = useState({
        officeNumber: postOffice?.officeNumber || '',
        address: postOffice?.address || ''
    });

    const handleSubmit = () => {
        onSave(formData);
        onClose();
    };

    return (
        <Dialog open onClose={onClose} maxWidth="sm" fullWidth>
            <DialogTitle>{postOffice ? 'Редактировать' : 'Новое'} отделение</DialogTitle>
            <DialogContent>
                <Grid container spacing={2} sx={{ mt: 1 }}>
                    <Grid item xs={12} md={6}>
                        <TextField
                            fullWidth
                            label="Номер отделения"
                            type="number"
                            value={formData.officeNumber}
                            onChange={(e) => setFormData({...formData, officeNumber: e.target.value})}
                        />
                    </Grid>
                    <Grid item xs={12} md={6}>
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

export default function PostOfficeList() {
    const [postOffices, setPostOffices] = useState([]);
    const [selectedPO, setSelectedPO] = useState(null);
    const [openForm, setOpenForm] = useState(false);
    const [filters, setFilters] = useState({ number: '', address: '' });
    const [details, setDetails] = useState({
        newspapers: [],
        printingHouses: [],
        loading: false
    });

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        try {
            const { data } = await postOfficeAPI.getAll();
            setPostOffices(data);
        } catch (error) {
            console.error('Ошибка загрузки:', error);
        }
    };

    const handleSave = async (data) => {
        try {
            if (selectedPO) {
                await postOfficeAPI.update(selectedPO.postOfficeID, data);
            } else {
                await postOfficeAPI.create(data);
            }
            loadData();
        } catch (error) {
            console.error('Ошибка сохранения:', error);
        }
    };

    const handleDelete = async (id) => {
        try {
            await postOfficeAPI.delete(id);
            loadData();
        } catch (error) {
            console.error('Ошибка удаления:', error);
        }
    };

    const loadDetails = async (id) => {
        setDetails(prev => ({...prev, loading: true}));
        try {
            const [newspapersRes, printHousesRes] = await Promise.all([
                postOfficeAPI.getNewspapers(id),
                postOfficeAPI.getPrintHouses(id)
            ]);

            setDetails({
                newspapers: newspapersRes.data,
                printingHouses: printHousesRes.data,
                loading: false
            });
        } catch (error) {
            console.error('Ошибка загрузки деталей:', error);
            setDetails(prev => ({...prev, loading: false}));
        }
    };

    const filteredData = postOffices.filter(po => {
        return po.officeNumber.toString().includes(filters.number) &&
            po.address.toLowerCase().includes(filters.address.toLowerCase());
    });

    return (
        <div className="container">
            <Typography variant="h4" gutterBottom>
                Управление почтовыми отделениями
            </Typography>

            <Grid container spacing={3}>
                <Grid item xs={12}>
                    <Button
                        variant="contained"
                        startIcon={<Add />}
                        onClick={() => {
                            setSelectedPO(null);
                            setOpenForm(true);
                        }}
                        sx={{ mb: 2 }}
                    >
                        Добавить отделение
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
                                        label="Поиск по номеру"
                                        type="number"
                                        value={filters.number}
                                        onChange={(e) => setFilters({...filters, number: e.target.value})}
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
                                    <TableCell>Номер</TableCell>
                                    <TableCell>Адрес</TableCell>
                                    <TableCell>Действия</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {filteredData.map(po => (
                                    <TableRow key={po.postOfficeID}>
                                        <TableCell>{po.officeNumber}</TableCell>
                                        <TableCell>{po.address}</TableCell>
                                        <TableCell>
                                            <IconButton onClick={() => {
                                                setSelectedPO(po);
                                                loadDetails(po.postOfficeID);
                                            }}>
                                                <Info color="info" />
                                            </IconButton>
                                            <IconButton onClick={() => {
                                                setSelectedPO(po);
                                                setOpenForm(true);
                                            }}>
                                                <Edit color="primary" />
                                            </IconButton>
                                            <IconButton onClick={() => handleDelete(po.postOfficeID)}>
                                                <Delete color="error" />
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Grid>

                {selectedPO && (
                    <Grid item xs={12}>
                        <Accordion defaultExpanded>
                            <AccordionSummary>
                                <Typography>Детали отделения #{selectedPO.officeNumber}</Typography>
                            </AccordionSummary>
                            <AccordionDetails>
                                {details.loading ? (
                                    <CircularProgress />
                                ) : (
                                    <>
                                        <Typography variant="h6" gutterBottom>
                                            Газеты в отделении:
                                        </Typography>
                                        <List dense>
                                            {details.newspapers.map(np => (
                                                <ListItem key={np.newspaperID}>
                                                    <ListItemText
                                                        primary={np.name}
                                                        secondary={`Количество: ${np.totalQuantity} экз.`}
                                                    />
                                                </ListItem>
                                            ))}
                                        </List>

                                        <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
                                            Поставляющие типографии:
                                        </Typography>
                                        <Grid container spacing={2}>
                                            {details.printingHouses.map(ph => (
                                                <Grid item xs={12} md={6} key={ph.printingHouseID}>
                                                    <Chip
                                                        label={ph.name}
                                                        variant="outlined"
                                                        sx={{ mr: 1, mb: 1 }}
                                                    />
                                                    <Typography variant="body2" color="textSecondary">
                                                        {ph.address}
                                                    </Typography>
                                                </Grid>
                                            ))}
                                        </Grid>
                                    </>
                                )}
                            </AccordionDetails>
                        </Accordion>
                    </Grid>
                )}
            </Grid>

            {openForm && (
                <PostOfficeForm
                    postOffice={selectedPO}
                    onClose={() => {
                        setOpenForm(false);
                        setSelectedPO(null);
                    }}
                    onSave={handleSave}
                />
            )}
        </div>
    );
}