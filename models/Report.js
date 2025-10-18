const mongoose = require('mongoose');

const reportSchema = new mongoose.Schema({
  subject: {
    type: String,
    required: true,
    enum: ['water', 'electric', 'sanitation', 'roads', 'animals', 'others']
  },
  subjectLabel: {
    type: String,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  details: {
    type: String,
    required: true
  },
  location: {
    type: String,
    required: true
  },
  fileUrl: {
    type: String
  },
  status: {
    type: String,
    enum: ['Pending', 'In Progress', 'Resolved'],
    default: 'Pending'
  },
  reply: {
    type: String,
    default: ''
  },
  userId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User'
  },
  createdAt: {
    type: Date,
    default: Date.now
  },
  updatedAt: {
    type: Date,
    default: Date.now
  }
});

module.exports = mongoose.model('Report', reportSchema);