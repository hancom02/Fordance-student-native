"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = require("express");
const supabase_1 = require("../config/supabase");
const auth_1 = require("../middleware/auth");
const router = (0, express_1.Router)();
// GET /api/v1/sessions/:id
router.get('/:id', auth_1.optionalAuth, async (req, res) => {
    try {
        const sessionId = req.params.id;
        const client = (0, supabase_1.getSupabaseClient)(req.token);
        const userId = req.user?.id;
        // 1. Fetch Session
        const { data: sessions, error: sessionErr } = await client
            .from('sessions')
            .select('*')
            .eq('id', sessionId)
            .limit(1);
        if (sessionErr || !sessions || sessions.length === 0) {
            res.status(404).json({ success: false, message: 'Session not found.' });
            return;
        }
        const session = sessions[0];
        // 2. Fetch Instructor
        let instructor = null;
        if (session.instructor_id) {
            const { data: instructors } = await client
                .from('users')
                .select('id, name, username, avatar_url, prize, introduce')
                .eq('id', session.instructor_id)
                .limit(1);
            instructor = instructors?.[0] || null;
        }
        // 3. Fetch Class summary if belongs to a class
        let danceClass = null;
        if (session.class_id) {
            const { data: classes } = await client
                .from('classes')
                .select('id, class_name, image_cover_url, level, genre')
                .eq('id', session.class_id)
                .limit(1);
            danceClass = classes?.[0] || null;
        }
        // 4. Check joined & favorite status
        let isJoined = false;
        let isFavorited = false;
        if (userId) {
            const { data: joinedPivot } = await client
                .from('users_sessions_joined')
                .select('id')
                .eq('user_id', userId)
                .eq('session_id', sessionId)
                .limit(1);
            isJoined = (joinedPivot && joinedPivot.length > 0) || false;
            const { data: favPivot } = await client
                .from('users_sessions_favourite')
                .select('id')
                .eq('user_id', userId)
                .eq('session_id', sessionId)
                .limit(1);
            isFavorited = (favPivot && favPivot.length > 0) || false;
        }
        res.json({
            success: true,
            data: {
                ...session,
                instructor,
                dance_class: danceClass,
                is_joined: isJoined,
                is_favourited: isFavorited,
            },
        });
    }
    catch (err) {
        res.status(500).json({ success: false, message: err.message || 'Failed to load session detail.' });
    }
});
// POST /api/v1/sessions/:id/join
router.post('/:id/join', auth_1.requireAuth, async (req, res) => {
    try {
        const sessionId = req.params.id;
        const userId = req.user.id;
        const client = (0, supabase_1.getSupabaseClient)(req.token);
        const { error } = await client
            .from('users_sessions_joined')
            .upsert({ user_id: userId, session_id: sessionId });
        if (error)
            throw error;
        res.json({
            success: true,
            message: 'Successfully joined session.',
            data: { session_id: sessionId, is_joined: true },
        });
    }
    catch (err) {
        res.status(500).json({ success: false, message: err.message || 'Failed to join session.' });
    }
});
// DELETE /api/v1/sessions/:id/join
router.delete('/:id/join', auth_1.requireAuth, async (req, res) => {
    try {
        const sessionId = req.params.id;
        const userId = req.user.id;
        const client = (0, supabase_1.getSupabaseClient)(req.token);
        const { error } = await client
            .from('users_sessions_joined')
            .delete()
            .eq('user_id', userId)
            .eq('session_id', sessionId);
        if (error)
            throw error;
        res.json({
            success: true,
            message: 'Successfully left session.',
            data: { session_id: sessionId, is_joined: false },
        });
    }
    catch (err) {
        res.status(500).json({ success: false, message: err.message || 'Failed to leave session.' });
    }
});
// POST /api/v1/sessions/:id/favorite
router.post('/:id/favorite', auth_1.requireAuth, async (req, res) => {
    try {
        const sessionId = req.params.id;
        const userId = req.user.id;
        const client = (0, supabase_1.getSupabaseClient)(req.token);
        const { error } = await client
            .from('users_sessions_favourite')
            .upsert({ user_id: userId, session_id: sessionId });
        if (error)
            throw error;
        res.json({
            success: true,
            message: 'Added session to favourites.',
            data: { session_id: sessionId, is_favourited: true },
        });
    }
    catch (err) {
        res.status(500).json({ success: false, message: err.message || 'Failed to add favourite.' });
    }
});
// DELETE /api/v1/sessions/:id/favorite
router.delete('/:id/favorite', auth_1.requireAuth, async (req, res) => {
    try {
        const sessionId = req.params.id;
        const userId = req.user.id;
        const client = (0, supabase_1.getSupabaseClient)(req.token);
        const { error } = await client
            .from('users_sessions_favourite')
            .delete()
            .eq('user_id', userId)
            .eq('session_id', sessionId);
        if (error)
            throw error;
        res.json({
            success: true,
            message: 'Removed session from favourites.',
            data: { session_id: sessionId, is_favourited: false },
        });
    }
    catch (err) {
        res.status(500).json({ success: false, message: err.message || 'Failed to remove favourite.' });
    }
});
exports.default = router;
