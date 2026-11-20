"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = require("express");
const supabase_1 = require("../config/supabase");
const router = (0, express_1.Router)();
// GET /api/v1/catalog/sessions
router.get('/sessions', async (req, res) => {
    try {
        const { q, level, genre, instructor_id } = req.query;
        let query = supabase_1.supabase.from('sessions').select('*');
        if (level && typeof level === 'string') {
            query = query.eq('level', level);
        }
        if (genre && typeof genre === 'string') {
            query = query.eq('genre', genre);
        }
        if (instructor_id && typeof instructor_id === 'string') {
            query = query.eq('instructor_id', instructor_id);
        }
        if (q && typeof q === 'string') {
            query = query.ilike('session_name', `%${q}%`);
        }
        const { data, error } = await query.limit(50);
        if (error)
            throw error;
        res.json({
            success: true,
            data: data || [],
        });
    }
    catch (err) {
        res.status(500).json({ success: false, message: err.message || 'Failed to search sessions.' });
    }
});
// GET /api/v1/catalog/classes
router.get('/classes', async (req, res) => {
    try {
        const { q, level, genre, instructor_id } = req.query;
        let query = supabase_1.supabase.from('classes').select('*');
        if (level && typeof level === 'string') {
            query = query.eq('level', level);
        }
        if (genre && typeof genre === 'string') {
            query = query.eq('genre', genre);
        }
        if (instructor_id && typeof instructor_id === 'string') {
            query = query.eq('instructor_id', instructor_id);
        }
        if (q && typeof q === 'string') {
            query = query.ilike('class_name', `%${q}%`);
        }
        const { data, error } = await query.limit(50);
        if (error)
            throw error;
        res.json({
            success: true,
            data: data || [],
        });
    }
    catch (err) {
        res.status(500).json({ success: false, message: err.message || 'Failed to search classes.' });
    }
});
// GET /api/v1/catalog/instructors
router.get('/instructors', async (_req, res) => {
    try {
        const { data, error } = await supabase_1.supabase
            .from('users')
            .select('id, name, username, avatar_url, prize, introduce')
            .eq('role', 'instructor')
            .limit(50);
        if (error)
            throw error;
        res.json({
            success: true,
            data: data || [],
        });
    }
    catch (err) {
        res.status(500).json({ success: false, message: err.message || 'Failed to fetch instructors.' });
    }
});
exports.default = router;
