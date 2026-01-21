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

class LoadUserDataEvent extends HomeEvent {
  const LoadUserDataEvent();

  @override
  List<Object?> get props => [];
}

class LoadTransactionsEvent extends HomeEvent {
  final dynamic localizations;

  const LoadTransactionsEvent({required this.localizations});

  @override
  List<Object?> get props => [localizations];
}

class InitializeHomeEvent extends HomeEvent {
  const InitializeHomeEvent();

  @override
  List<Object?> get props => [];
}



