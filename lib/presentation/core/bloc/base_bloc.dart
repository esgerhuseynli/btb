import 'package:flutter_bloc/flutter_bloc.dart';

abstract class BaseBloc<Event, State> extends Bloc<Event, State> {
  BaseBloc(super.initialState);

  @override
  void onEvent(Event event) {
    super.onEvent(event);
    // Add any common event handling here
  }

  @override
  void onChange(Change<State> change) {
    super.onChange(change);
    // Add any common state change handling here
  }

  @override
  void onError(Object error, StackTrace stackTrace) {
    super.onError(error, stackTrace);
    // Add common error handling here
  }
}



