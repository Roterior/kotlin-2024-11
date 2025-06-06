# User Stories для автозаправки (АЗС)

## User Stories помогают понять потребности разных пользователей и улучшить сервис.

1. Для водителей (клиентов)
    1. Заправка топливом
        1. Как водитель, я хочу быстро заправить бак, чтобы сэкономить время.
            Критерии приемки:
            - На АЗС есть свободные колонки или система очереди.
            - Топливо подается без задержек (время заправки ≤ 5 минут).
            - На колонке есть инструкция или подсказки для самостоятельной заправки.
        2. Как путешественник, я хочу знать цены на топливо заранее, чтобы спланировать бюджет.
            Критерии приемки:
            - Цены видны на табло перед въездом.
            - Актуальные цены есть в мобильном приложении/на сайте.
            - Указаны все виды топлива (АИ-92, 95, 98, ДТ).
    2. Оплата и скидки
        1. Как клиент, я хочу оплачивать топливо картой на колонке, чтобы не стоять в очереди в кассу.
            Критерии приемки:
            - На колонке есть терминал с бесконтактной оплатой.
            - Оплата проходит за ≤ 30 секунд.
            - Чек приходит на email/SMS (по желанию).
        2. Как постоянный клиент, я хочу получать скидки по бонусной карте, чтобы экономить на заправках.
            Критерии приемки:
            - Скидка автоматически применяется при оплате.
            - Баланс бонусов отображается в чеке/приложении.
            - Можно проверить историю начислений.
    3. Дополнительные услуги
        1. Как дальнобойщик, я хочу принять душ на АЗС, чтобы чувствовать себя свежим в дороге.
            Критерии приемки:
            - Душ доступен 24/7 или в указанные часы.
            - Чистое помещение, есть полотенца/гель (платно/бесплатно).
            - Оплата через терминал/кассу.
2. Для сотрудников АЗС
    1. Обслуживание клиентов
        1. Как оператор, я хочу видеть остатки топлива в резервуарах, чтобы избежать нехватки.
            Критерии приемки:
            - Данные обновляются в реальном времени.
            - Система отправляет уведомление при запасе ≤ 10%.
            - Можно сформировать отчет за любой период.
        2. Как кассир, я хочу быстро пробивать покупки, чтобы уменьшить очереди.
            Критерии приемки:
            - 1 товар сканируется ≤ 3 секунды. 
            - Есть горячие клавиши для популярных товаров. 
            - Поддержка оплаты QR-кодами (СБП, Alipay).
3. Для владельцев/управляющих
    1. Как владелец, я хочу видеть ежедневную выручку по видам топлива, чтобы анализировать спрос.
        Критерии приемки:
        - Дашборд с графиками по топливу/товарам. 
        - Возможность выгрузить данные в Excel. 
        - Доступ с любого устройства.
4. Для B2B-клиентов
    1. Как менеджер автопарка, я хочу удалённо отслеживать расход топлива по картам.
        Критерии приемки:
        - Онлайн-отчеты по каждой карте/водителю. 
        - Настройка лимитов и уведомлений. 
        - API для интеграции с 1С/ERP.
