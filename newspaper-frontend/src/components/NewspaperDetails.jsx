import React, { useState, useEffect } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    Typography,
    List,
    ListItem,
    ListItemText,
    Chip,
    Divider,
    CircularProgress
} from '@mui/material';
import { newspaperAPI } from '../services/api';
import { LocalPrintshop, LocalPostOffice, AttachMoney, Edit } from '@mui/icons-material';

export default function NewspaperDetails({ newspaper, onClose }) {
    const [details, setDetails] = useState({
        printingHouses: [],
        postOffices: [],
        totalCost: null,
        loading: true
    });

    useEffect(() => {
        const loadDetails = async () => {
            try {
                const [printingResponse, costResponse] = await Promise.all([
                    newspaperAPI.getPrintHouses(newspaper.newspaperID),
                    newspaperAPI.getTotalCost(newspaper.newspaperID)
                ]);

                setDetails({
                    printingHouses: printingResponse.data,
                    totalCost: costResponse.data,
                    loading: false
                });
            } catch (error) {
                console.error('Error loading details:', error);
                setDetails(prev => ({ ...prev, loading: false }));
            }
        };

        if (newspaper) {
            loadDetails();
        }
    }, [newspaper]);

    return (
        <Dialog open={!!newspaper} onClose={onClose} maxWidth="md" fullWidth>
            <DialogTitle>
                <Typography variant="h5">
                    {newspaper?.title} Details
                    <Chip
                        label={`Index: ${newspaper?.publicationIndex}`}
                        variant="outlined"
                        sx={{ ml: 2 }}
                    />
                </Typography>
            </DialogTitle>

            <DialogContent dividers>
                {details.loading ? (
                    <CircularProgress sx={{ display: 'block', margin: '20px auto' }} />
                ) : (
                    <div className="details-container">
                        <section className="basic-info">
                            <Typography variant="h6" gutterBottom>
                                <Edit sx={{ verticalAlign: 'middle', mr: 1 }} />
                                General Information
                            </Typography>
                            <List dense>
                                <ListItem>
                                    <ListItemText
                                        primary="Editor"
                                        secondary={newspaper.editor}
                                    />
                                </ListItem>
                                <ListItem>
                                    <ListItemText
                                        primary="Price per copy"
                                        secondary={`$${newspaper.price?.toFixed(2)}`}
                                    />
                                </ListItem>
                                <ListItem>
                                    <ListItemText
                                        primary="Publication Date"
                                        secondary={newspaper.publicationDate}
                                    />
                                </ListItem>
                            </List>
                        </section>

                        <Divider sx={{ my: 3 }} />

                        <section className="printing-houses">
                            <Typography variant="h6" gutterBottom>
                                <LocalPrintshop sx={{ verticalAlign: 'middle', mr: 1 }} />
                                Associated Printing Houses
                            </Typography>
                            {details.printingHouses.length > 0 ? (
                                <List dense>
                                    {details.printingHouses.map(ph => (
                                        <ListItem key={ph.printingHouseID}>
                                            <ListItemText
                                                primary={ph.name}
                                                secondary={`Address: ${ph.address}`}
                                            />
                                        </ListItem>
                                    ))}
                                </List>
                            ) : (
                                <Typography variant="body2" color="textSecondary">
                                    No printing houses found
                                </Typography>
                            )}
                        </section>

                        <Divider sx={{ my: 3 }} />

                        <section className="financial-info">
                            <Typography variant="h6" gutterBottom>
                                <AttachMoney sx={{ verticalAlign: 'middle', mr: 1 }} />
                                Financial Summary
                            </Typography>
                            <List dense>
                                <ListItem>
                                    <ListItemText
                                        primary="Total Cost of All Print Runs"
                                        secondary={
                                            details.totalCost !== null
                                                ? `$${details.totalCost.toFixed(2)}`
                                                : 'Calculating...'
                                        }
                                    />
                                </ListItem>
                            </List>
                        </section>

                        <Divider sx={{ my: 3 }} />

                        <section className="post-offices">
                            <Typography variant="h6" gutterBottom>
                                <LocalPostOffice sx={{ verticalAlign: 'middle', mr: 1 }} />
                                Connected Post Offices
                            </Typography>
                            <Typography variant="body2" color="textSecondary">
                                Feature implementation in progress...
                            </Typography>
                        </section>
                    </div>
                )}
            </DialogContent>

            <DialogActions>
                <Button onClick={onClose} variant="outlined">
                    Close
                </Button>
            </DialogActions>
        </Dialog>
    );
}