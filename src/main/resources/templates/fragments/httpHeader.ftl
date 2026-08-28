<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <#if seo?? >
    <title>${seo.title}</title>
    <meta name="description" content="${seo.description! "ServiceHub - создай заказ и получи результат через 5 минут"}"/>
        <#else>
            <title>Инновационная площадка для фрилансеров и заказчиков которая решает проблемы</title>
            <meta name="description" content="ServiceHub - создай заказ и получи результат через 5 минут"/>
    </#if>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="/main.css">

    <style>
        body {
            background-color: #f4f6f9;
        }

        .dashboard-card {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 12px;
            box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
            border: none;
        }
        .logo-heart {
            color: #e74c3c;
        }
        .stat-card {
            transition: transform 0.2s;
        }
        .stat-card:hover {
            transform: translateY(-4px);
        }
        .sidebar-nav .nav-link {
            color: #555;
            padding: 1rem 1.25rem;
            border-radius: 8px;
        }
        .sidebar-nav .nav-link.active,
        .sidebar-nav .nav-link:hover {
            color: #667eea;
            background-color: rgba(102, 126, 234, 0.1);
        }
        .user-avatar {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            object-fit: cover;
            border: 2px solid #fff;
            box-shadow: 0 2px 8px rgba(0,0,0,0.15);
        }

    </style>
    <style>
        .toolbar-main {
            border: none!important;
        }
        .toolbar-header {
            border: 1px solid #8d822c!important;
            border-radius: 10px!important;
        }
    </style>
    <style>
            .filter-section {
                margin-bottom: 30px;
                background: white;
                border-radius: 10px;
                padding: 10px;
                border: 1px solid #8d822c;
            }
            .filter-title {
                font-weight: 600;
                margin-bottom: 15px;
                color: #333;
                font-size: 1.1rem;
            }
            .form-check-label {
                cursor: pointer;
                padding-left: 10px;
            }
            .category-item {
                padding-left: 15px;
                margin-bottom: 8px;
            }
            .category-item.parent {
                font-weight: 500;
                margin-top: 10px;
            }
            .order-card {
                border-radius: 15px;
                border: 1px solid #8d822c;
                padding: 20px;
                margin-bottom: 20px;
                transition: all 0.3s;
                background: white;
            }
            .order-card:hover {
                box-shadow: 0 8px 25px rgba(0,0,0,0.1);
                border-color: #0d6efd;
            }
            .main-content {
                padding: 0px 0;
            }
            .footer-custom {
                background-color: #343a40;
                color: white;
                padding: 15px 0;
                margin-top: 40px;
            }
        </style>

         <style>

                .login-card {
                    background: rgba(255, 255, 255, 0.95);
                    backdrop-filter: blur(10px);
                    border-radius: 15px;
                    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
                }
                .logo-heart {
                    color: #e74c3c;
                    font-size: 2.5rem;
                }
                .form-control:focus {
                    border-color: #667eea;
                    box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
                }

            </style>


</head>