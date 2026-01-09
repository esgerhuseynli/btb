import 'package:equatable/equatable.dart';
import '../../../data/models/bank_card.dart';
import '../../../data/models/bank_account.dart';

abstract class HomeState extends Equatable {
  const HomeState();

  @override
  List<Object?> get props => [];
}

class HomeInitial extends HomeState {
  const HomeInitial();
}

class HomeLoading extends HomeState {
  const HomeLoading();
}

class HomeLoaded extends HomeState {
  final List<BankCard> bankCards;
  final List<BankAccount> bankAccounts;

  const HomeLoaded({
    required this.bankCards,
    required this.bankAccounts,
  });

  @override
  List<Object?> get props => [bankCards, bankAccounts];
}

class HomeError extends HomeState {
  final String message;

  const HomeError(this.message);

  @override
  List<Object?> get props => [message];
}



