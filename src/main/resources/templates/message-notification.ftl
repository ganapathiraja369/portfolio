<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>New Message Notification</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f4f4;
            padding: 20px;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }
        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px 20px;
            text-align: center;
        }
        .header h1 {
            font-size: 24px;
            margin-bottom: 5px;
        }
        .content {
            padding: 30px 20px;
        }
        .message-box {
            background-color: #f8f9fa;
            border-left: 4px solid #667eea;
            padding: 15px;
            margin: 20px 0;
            border-radius: 4px;
        }
        .message-box p {
            margin: 8px 0;
            color: #333;
            line-height: 1.6;
        }
        .message-content {
            background-color: #ffffff;
            border: 1px solid #ddd;
            padding: 15px;
            border-radius: 4px;
            margin: 15px 0;
            font-style: italic;
            color: #555;
            line-height: 1.6;
        }
        .details {
            margin: 20px 0;
        }
        .detail-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }
        .detail-row:last-child {
            border-bottom: none;
        }
        .detail-label {
            font-weight: 600;
            color: #333;
        }
        .detail-value {
            color: #666;
            word-break: break-word;
        }
        .footer {
            background-color: #f8f9fa;
            padding: 20px;
            text-align: center;
            border-top: 1px solid #eee;
            color: #666;
            font-size: 12px;
        }
        .button {
            display: inline-block;
            background-color: #667eea;
            color: white;
            padding: 12px 30px;
            text-decoration: none;
            border-radius: 4px;
            margin: 20px 0;
            text-align: center;
        }
        .button:hover {
            background-color: #764ba2;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📨 New Message Received</h1>
            <p>You have a new message in your Portfolio</p>
        </div>
        
        <div class="content">
            <div class="message-box">
                <p><strong>Hello,</strong></p>
                <p>You have received a new message. Here are the details:</p>
            </div>
            
            <div class="details">
                <div class="detail-row">
                    <span class="detail-label">From Name:</span>
                    <span class="detail-value">${senderName}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">From Email:</span>
                    <span class="detail-value">${senderEmail}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Fingerprint:</span>
                    <span class="detail-value">${fingerPrint}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Date & Time:</span>
                    <span class="detail-value">${timestamp}</span>
                </div>
            </div>
            
            <p><strong>Message:</strong></p>
            <div class="message-content">
                ${messageContent}
            </div>
            
            <p style="margin-top: 20px; color: #666; font-size: 14px;">
                This is an automated notification from your Portfolio API. Please do not reply to this email.
            </p>
        </div>
        
        <div class="footer">
            <p>&copy; 2026 Portfolio API. All rights reserved.</p>
            <p>If you did not expect this message, please contact support.</p>
        </div>
    </div>
</body>
</html>
