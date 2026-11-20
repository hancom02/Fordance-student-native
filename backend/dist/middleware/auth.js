"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.optionalAuth = exports.requireAuth = void 0;
const supabase_1 = require("../config/supabase");
const requireAuth = async (req, res, next) => {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
        res.status(401).json({
            success: false,
            message: 'Authorization header missing or invalid format. Expected Bearer <token>.',
        });
        return;
    }
    const token = authHeader.substring(7).trim();
    try {
        const { data: { user }, error } = await supabase_1.supabase.auth.getUser(token);
        if (error || !user) {
            res.status(401).json({
                success: false,
                message: 'Invalid or expired token.',
                error: error?.message,
            });
            return;
        }
        req.user = user;
        req.token = token;
        next();
    }
    catch (err) {
        res.status(500).json({
            success: false,
            message: 'Failed to authenticate user.',
            error: err?.message,
        });
    }
};
exports.requireAuth = requireAuth;
const optionalAuth = async (req, _res, next) => {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
        return next();
    }
    const token = authHeader.substring(7).trim();
    try {
        const { data: { user } } = await supabase_1.supabase.auth.getUser(token);
        if (user) {
            req.user = user;
            req.token = token;
        }
    }
    catch {
        // Graceful fallback for unauthenticated requests
    }
    next();
};
exports.optionalAuth = optionalAuth;
