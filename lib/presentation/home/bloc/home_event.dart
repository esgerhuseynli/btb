import 'package:equatable/equatable.dart';

abstract class HomeEvent extends Equatable {
  const HomeEvent();

  @override
  List<Object?> get props => [];
}

class LoadBankCardsEvent extends HomeEvent {
  const LoadBankCardsEvent();
}

class LoadBankAccountsEvent extends HomeEvent {
  const LoadBankAccountsEvent();
}

class RefreshHomeDataEvent extends HomeEvent {
  const RefreshHomeDataEvent();
}



