import { Router, Request, Response } from 'express';
import { supabase } from '../config/supabase';

const router = Router();

// GET /api/v1/classes/:id
router.get('/:id', async (req: Request, res: Response) => {
  try {
    const classId = req.params.id;

    // 1. Fetch Class
    const { data: classes, error: classErr } = await supabase
      .from('classes')
      .select('*')
      .eq('id', classId)
      .limit(1);

    if (classErr || !classes || classes.length === 0) {
      res.status(404).json({ success: false, message: 'Class not found.' });
      return;
    }

    const danceClass = classes[0];

    // 2. Fetch Instructor
    let instructor: any = null;
    if (danceClass.instructor_id) {
      const { data: instructors } = await supabase
        .from('users')
        .select('id, name, username, avatar_url, prize, introduce')
        .eq('id', danceClass.instructor_id)
        .limit(1);
      instructor = instructors?.[0] || null;
    }

    // 3. Fetch Sessions belonging to this class
    const { data: sessions, error: sessionErr } = await supabase
      .from('sessions')
      .select('*')
      .eq('class_id', classId)
      .neq('status', 'deleted')
      .order('id', { ascending: true });

    if (sessionErr) throw sessionErr;

    res.json({
      success: true,
      data: {
        ...danceClass,
        instructor,
        sessions: sessions || [],
        total_sessions: sessions?.length || 0,
      },
    });
  } catch (err: any) {
    res.status(500).json({ success: false, message: err.message || 'Failed to load class detail.' });
  }
});

export default router;
