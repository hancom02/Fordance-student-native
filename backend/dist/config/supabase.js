"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.getSupabaseClient = exports.supabase = exports.SUPABASE_ANON_KEY = exports.SUPABASE_URL = void 0;
const supabase_js_1 = require("@supabase/supabase-js");
const dotenv_1 = __importDefault(require("dotenv"));
dotenv_1.default.config();
exports.SUPABASE_URL = process.env.SUPABASE_URL || 'https://agqjmztarfgnmwipiklw.supabase.co';
exports.SUPABASE_ANON_KEY = process.env.SUPABASE_ANON_KEY || 'sb_publishable_n7Fs6zIUNZins7SgtdR0vA_CaChKiBV';
exports.supabase = (0, supabase_js_1.createClient)(exports.SUPABASE_URL, exports.SUPABASE_ANON_KEY, {
    auth: {
        persistSession: false,
        autoRefreshToken: false,
    },
});
const getSupabaseClient = (token) => {
    if (!token)
        return exports.supabase;
    return (0, supabase_js_1.createClient)(exports.SUPABASE_URL, exports.SUPABASE_ANON_KEY, {
        auth: {
            persistSession: false,
            autoRefreshToken: false,
        },
        global: {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        },
    });
};
exports.getSupabaseClient = getSupabaseClient;
