<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Message Received Confirmation</title>
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
            background: linear-gradient(135deg, #48c774 0%, #3b7d39 100%);
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
        .confirmation-box {
            background-color: #f0fdf4;
            border-left: 4px solid #48c774;
            padding: 15px;
            margin: 20px 0;
            border-radius: 4px;
        }
        .confirmation-box p {
            margin: 8px 0;
            color: #333;
            line-height: 1.6;
        }
        .reference-box {
            background-color: #f8f9fa;
            border: 1px solid #ddd;
            padding: 15px;
            border-radius: 4px;
            margin: 20px 0;
        }
        .reference-box p {
            margin: 8px 0;
            color: #555;
        }
        .ref-label {
            font-weight: 600;
            color: #333;
            font-size: 12px;
        }
        .ref-value {
            word-break: break-all;
            font-family: 'Courier New', monospace;
            color: #666;
            margin-top: 5px;
        }
        .footer {
            background-color: #f8f9fa;
            padding: 20px;
            text-align: center;
            border-top: 1px solid #eee;
            color: #666;
            font-size: 12px;
        }
        .highlight {
            background-color: #fffacd;
            padding: 2px 6px;
            border-radius: 3px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>✅ Message Received</h1>
            <p>Your message has been successfully submitted</p>
        </div>
        
        <div class="content">
            <div class="confirmation-box">
                <p><strong>Thank you for reaching out!</strong></p>
                <p>Your message has been successfully submitted and received. We appreciate you taking the time to contact us.</p>
            </div>
            
            <h2 style="font-size: 18px; margin: 20px 0 10px 0; color: #333;">Message Details:</h2>
            <div class="reference-box">
                <p>
                    <span class="ref-label">Your Name:</span>
                    <div class="ref-value">${senderName}</div>
                </p>
                <p style="margin-top: 15px;">
                    <span class="ref-label">Your Email:</span>
                    <div class="ref-value">${senderEmail}</div>
                </p>
                <p style="margin-top: 15px;">
                    <span class="ref-label">Message ID:</span>
                    <div class="ref-value">${messageId}</div>
                </p>
                <p style="margin-top: 15px;">
                    <span class="ref-label">Submission Time:</span>
                    <div class="ref-value">${timestamp}</div>
                </p>
            </div>
            
            <p style="margin: 20px 0 10px 0; color: #666; font-size: 14px;">
                Your message has been forwarded to our team for review. We will get back to you as soon as possible.
            </p>
            
            <p style="margin: 10px 0; color: #666; font-size: 14px;">
                <strong>Please keep your Message ID:</strong> <span class="highlight">${messageId}</span> for reference.
            </p>
        </div>
        
        <div class="footer">
            <p>&copy; 2026 Portfolio API. All rights reserved.</p>
            <p>This is an automated confirmation. Please do not reply to this email.</p>
        </div>
    </div>
</body>
</html>
