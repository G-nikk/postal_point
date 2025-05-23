import React from 'react';
import { useForm } from 'react-hook-form';
import { newspaperAPI } from '../services/api';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    TextField,
    DialogActions,
    Button,
    Grid,
    FormHelperText
} from '@mui/material';

export default function NewspaperForm({ newspaper, onClose }) {
    const { register, handleSubmit, formState: { errors } } = useForm({
        defaultValues: newspaper || {
            name: '',
            indexEdition: '',
            editor: '',
            price: 0,
            publicationDate: new Date().toISOString().split('T')[0]
        }
    });

    const onSubmit = async (data) => {
        try {
            if (newspaper) {
                await newspaperAPI.update(newspaper.newspaperID, data);
            } else {
                await newspaperAPI.create(data);
            }
            onClose();
        } catch (error) {
            console.error('Submission error:', error.response?.data);
        }
    };

    return (
        <Dialog open onClose={onClose} maxWidth="sm" fullWidth>
            <DialogTitle>{newspaper ? 'Edit' : 'New'} Newspaper</DialogTitle>
            <form onSubmit={handleSubmit(onSubmit)}>
                <DialogContent>
                    <Grid container spacing={2}>
                        <Grid item xs={12}>
                            <TextField
                                fullWidth
                                label="Name"
                                {...register('name', { required: 'Name is required' })}
                                error={!!errors.title}
                                helperText={errors.title?.message}
                            />
                        </Grid>
                        <Grid item xs={12} md={6}>
                            <TextField
                                fullWidth
                                label="Index Edition"
                                {...register('indexEdition', {
                                    required: 'Index is required',
                                    pattern: {
                                        value: /^[A-Za-z0-9-]+$/,
                                        message: 'Invalid index format'
                                    }
                                })}
                                error={!!errors.publicationIndex}
                                helperText={errors.publicationIndex?.message}
                            />
                        </Grid>
                        <Grid item xs={12} md={6}>
                            <TextField
                                fullWidth
                                label="Editor"
                                {...register('editor', { required: 'Editor is required' })}
                                error={!!errors.editor}
                                helperText={errors.editor?.message}
                            />
                        </Grid>
                        <Grid item xs={12} md={6}>
                            <TextField
                                fullWidth
                                label="Price"
                                type="number"
                                inputProps={{ step: "0.01" }}
                                {...register('price', {
                                    required: 'Price is required',
                                    min: { value: 0, message: 'Price must be positive' }
                                })}
                                error={!!errors.price}
                                helperText={errors.price?.message}
                            />
                        </Grid>
                        <Grid item xs={12} md={6}>
                            <TextField
                                fullWidth
                                label="Publication Date"
                                type="date"
                                InputLabelProps={{ shrink: true }}
                                {...register('publicationDate')}
                            />
                        </Grid>
                    </Grid>
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose}>Cancel</Button>
                    <Button type="submit" variant="contained" color="primary">
                        {newspaper ? 'Update' : 'Create'}
                    </Button>
                </DialogActions>
            </form>
        </Dialog>
    );
}