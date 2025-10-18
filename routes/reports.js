const express = require('express');
const router = express.Router();
const Report = require('../models/Report');
const auth = require('../middleware/auth');

// Get all reports
router.get('/', async (req, res) => {
  try {
    const reports = await Report.find().sort({ createdAt: -1 });
    res.json(reports);
  } catch (error) {
    res.status(500).json({ message: 'Error fetching reports' });
  }
});

// Create new report
router.post('/', auth, async (req, res) => {
  try {
    const { subject, subjectLabel, title, details, location, fileUrl } = req.body;

    const report = new Report({
      subject,
      subjectLabel,
      title,
      details,
      location,
      fileUrl,
      userId: req.user.userId
    });

    await report.save();
    res.status(201).json(report);
  } catch (error) {
    res.status(500).json({ message: 'Error creating report' });
  }
});

// Admin reply to report
router.patch('/:id/reply', auth, async (req, res) => {
  try {
    if (req.user.role !== 'admin') {
      return res.status(403).json({ message: 'Admin access required' });
    }

    const { reply, status } = req.body;
    const report = await Report.findById(req.params.id);

    if (!report) {
      return res.status(404).json({ message: 'Report not found' });
    }

    report.reply = reply;
    report.status = status || 'In Progress';
    report.updatedAt = Date.now();

    await report.save();
    res.json(report);
  } catch (error) {
    res.status(500).json({ message: 'Error updating report' });
  }
});

// Get user's reports
router.get('/user', auth, async (req, res) => {
  try {
    const reports = await Report.find({ userId: req.user.userId }).sort({ createdAt: -1 });
    res.json(reports);
  } catch (error) {
    res.status(500).json({ message: 'Error fetching user reports' });
  }
});

// Get single report
router.get('/:id', async (req, res) => {
  try {
    const report = await Report.findById(req.params.id);
    if (!report) {
      return res.status(404).json({ message: 'Report not found' });
    }
    res.json(report);
  } catch (error) {
    res.status(500).json({ message: 'Error fetching report' });
  }
});

module.exports = router;