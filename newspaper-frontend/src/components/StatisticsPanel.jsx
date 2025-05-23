import React from 'react';
import {
    Box,
    Typography,
    List,
    ListItem,
    ListItemText,
    Divider,
    Chip,
    CircularProgress,
    Accordion,
    AccordionSummary,
    AccordionDetails
} from '@mui/material';
import { LocalPrintshop, People, AttachMoney } from '@mui/icons-material';

export default function StatisticsPanel({ newspaper, stats, onEditorLoad }) {
    if (!newspaper) return (
        <Box sx={{ p: 2, textAlign: 'center' }}>
            <Typography variant="subtitle1">
                Select a newspaper to view details
            </Typography>
        </Box>
    );

    return (
        <Box sx={{ p: 2, bgcolor: 'background.paper', borderRadius: 2 }}>
            <Typography variant="h6" gutterBottom>
                {newspaper.title} Details
            </Typography>

            <Accordion defaultExpanded>
                <AccordionSummary>
                    <Typography><LocalPrintshop /> Printing Houses</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    {stats.printingHouses.length > 0 ? (
                        <List dense>
                            {stats.printingHouses.map(ph => (
                                <ListItem key={ph.printingHouseID}>
                                    <ListItemText
                                        primary={ph.name}
                                        secondary={`${ph.address} • ${ph.printRuns?.length || 0} print runs`}
                                    />
                                    <Chip
                                        label="Get Editor"
                                        onClick={() => onEditorLoad(ph.printingHouseID)}
                                        variant="outlined"
                                    />
                                </ListItem>
                            ))}
                        </List>
                    ) : (
                        <Typography variant="body2">No printing houses found</Typography>
                    )}
                </AccordionDetails>
            </Accordion>

            <Accordion>
                <AccordionSummary>
                    <Typography><AttachMoney /> Financial Info</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                        <Typography variant="body1">
                            Total Cost of Print Runs:
                        </Typography>
                        {stats.totalCost !== null ? (
                            <Chip
                                label={`$${stats.totalCost.toFixed(2)}`}
                                color="success"
                                variant="outlined"
                            />
                        ) : (
                            <CircularProgress size={24} />
                        )}
                    </Box>
                </AccordionDetails>
            </Accordion>

            {stats.editorInfo && (
                <Accordion>
                    <AccordionSummary>
                        <Typography><People /> Editor Information</Typography>
                    </AccordionSummary>
                    <AccordionDetails>
                        <Typography variant="body1">
                            Editor for largest print run: {stats.editorInfo}
                        </Typography>
                    </AccordionDetails>
                </Accordion>
            )}
        </Box>
    );
}