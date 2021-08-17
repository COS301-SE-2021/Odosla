import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:odosla/provider/cart_provider.dart';
import 'package:odosla/services/api_service.dart';
import 'dart:async';

import 'package:provider/provider.dart';
import 'package:rflutter_alert/rflutter_alert.dart';

class StatusProvider with ChangeNotifier {
  String _status = "PENDING";
  Map<String, double> p = {
    'PENDING': 0.0,
    'AWAITING_PAYMENT': 0.1,
    'VERIFYING': 0.2,
    'PURCHASED': 0.3,
    'IN_QUEUE': 0.4,
    'PACKING': 0.5,
    'AWAITING_COLLECTION': 0.69,
    'DELIVERY_COLLECTED': 0.84,
    'CUSTOMER_COLLECTED': 1.0,
    'DELIVERED': 1.0,
  };
  Map<String, String> t = {
    'PENDING': 'Requesting order..',
    'AWAITING_PAYMENT': 'Awaiting payment..',
    'VERIFYING': 'Verifying payment..',
    'PURCHASED': 'Payment received..',
    'IN_QUEUE': 'In queue..',
    'PACKING': 'Packing cart..',
    'AWAITING_COLLECTION': 'Awaiting collection..',
    'DELIVERY_COLLECTED': 'Collected by driver..',
    'CUSTOMER_COLLECTED': 'Collected by customer',
    'DELIVERED': 'Order delivered',
  };

  // Timer timer = Timer(, callback);

  StatusProvider() {}

  String get status {
    return t[_status] as String;
  }

  set status(String value) {
    _status = value;
    notifyListeners();
  }

  double get progress {
    return p[_status] as double;
  }
}
