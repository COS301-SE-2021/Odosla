import 'package:flutter/cupertino.dart';
import 'package:odosla/provider/driver_provider.dart';
import 'package:odosla/services/api_service.dart';
import 'package:provider/provider.dart';
import 'package:rating_dialog/rating_dialog.dart';

class Rating {
  ApiService api = ApiService();

  _dialog(BuildContext context) => RatingDialog(
        // your app's name?
        title: 'Rating Dialog',
        // encourage your user to leave a high rating?
        message:
            'Tap a star to set your rating. Add more description here if you want.',
        // your app's logo?
        submitButton: 'Submit',
        onCancelled: () => print('cancelled'),
        onSubmitted: (response) {
          api.rateDriver(
              Provider.of<DriverProvider>(context).id, response.rating);
        },
      );
}
