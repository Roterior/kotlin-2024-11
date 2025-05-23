# Функциональные требования

## Функциональные требования к автозаправке (АЗС).

1. Управление топливными операциями
    - Система должна принимать и обрабатывать запросы на заправку от клиентов (через оператора или самообслуживание). 
    - Возможность выбора типа топлива (АИ-92, АИ-95, ДТ и др.) с отображением актуальных цен. 
    - Контроль остатков топлива в резервуарах с автоматическим оповещением о низком уровне (≤10%). 
    - Блокировка заправки при отсутствии связи с платежной системой или ошибках оборудования.
2. Оплата и финансовый учет
    - Поддержка разных способов оплаты:
      - Наличные (через кассира). 
      - Банковские карты (NFC, чип, магнитная полоса). 
      - Мобильные платежи (QR, Apple Pay/Google Pay). 
      - Топливные карты (корпоративные и личные). 
    - Автоматическая печать/отправка чека (SMS, email, бумажный). 
    - Интеграция с бухгалтерскими системами (1С, ERP) для учета продаж. 
    - Возврат средств при ошибочной оплате (в течение 15 минут).
3. Безопасность и контроль
    - Аутентификация сотрудников (логин/пароль или карта доступа) для работы с системой. 
    - Видеонаблюдение за зоной заправки, касс и территории (архив — 30 дней). 
    - Датчики утечки топлива и автоматическое отключение подачи при аварии. 
    - Антимошеннические механизмы (блокировка колонки при подозрительных операциях).
4. Управление дополнительными услугами
    - Продажа сопутствующих товаров (масла, автохимия, напитки) через кассу. 
    - Модуль мойки автомобилей (интеграция с системой оплаты АЗС). 
    - Система лояльности:
      - Начисление бонусов за заправку. 
      - Персональные скидки для постоянных клиентов.
5. Отчетность и аналитика
    - Генерация отчетов по продажам топлива/товаров (ежедневно, еженедельно, помесячно). 
    - Контроль топливных остатков и автоматический заказ поставок при достижении минимального уровня. 
    - Мониторинг загруженности АЗС (пиковые часы, популярные услуги).
6. Техническая инфраструктура
    - Резервное питание (генератор) для работы касс и колонок при отключении электричества. 
    - Удаленное управление оборудованием (диагностика, обновление ПО). 
    - API для интеграции с внешними сервисами (Google Maps, гос. системы).
7. Требования к пользовательскому интерфейсу
    - Интуитивный интерфейс терминалов самообслуживания (подсказки, поддержка 2+ языков). 
    - Мобильное приложение для клиентов:
      - Поиск ближайшей АЗС. 
      - Просмотр акций и баланса бонусов. 
      - Бесконтактная оплата.
