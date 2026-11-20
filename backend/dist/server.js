"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const cors_1 = __importDefault(require("cors"));
const dotenv_1 = __importDefault(require("dotenv"));
const auth_1 = __importDefault(require("./routes/auth"));
const discover_1 = __importDefault(require("./routes/discover"));
const catalog_1 = __importDefault(require("./routes/catalog"));
const sessions_1 = __importDefault(require("./routes/sessions"));
const classes_1 = __importDefault(require("./routes/classes"));
dotenv_1.default.config();
const app = (0, express_1.default)();
const port = process.env.PORT || 3000;
// Middlewares
app.use((0, cors_1.default)());
app.use(express_1.default.json());
// Routes
app.use('/api/v1/auth', auth_1.default);
app.use('/api/v1/discover', discover_1.default);
app.use('/api/v1/catalog', catalog_1.default);
app.use('/api/v1/sessions', sessions_1.default);
app.use('/api/v1/classes', classes_1.default);
// Health check
app.get('/health', (_req, res) => {
    res.json({
        status: 'ok',
        service: 'fordance-backend',
        version: '1.0.0',
        timestamp: new Date().toISOString(),
    });
});
// Central Error Handler
app.use((err, _req, res, _next) => {
    console.error('Unhandled server error:', err);
    res.status(500).json({
        success: false,
        message: err.message || 'Internal server error',
    });
});
if (process.env.NODE_ENV !== 'test') {
    app.listen(port, () => {
        console.log(`[FOR DANCE Backend] Server running at http://localhost:${port}`);
        console.log(`[FOR DANCE Backend] Health check: http://localhost:${port}/health`);
    });
}
exports.default = app;
